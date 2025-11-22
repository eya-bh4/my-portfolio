import copy
import time
import random
import tkinter as tk
from tkinter import ttk 
from collections import deque
import heapq
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg

def verifier_grille_initiale(grille):
    #détecter des redondances
    for i in range(9):
        ligne = [v for v in grille[i] if v != 0]
        if len(ligne) != len(set(ligne)):
            return False
    for j in range(9):
        colonne = [grille[i][j] for i in range(9) if grille[i][j] != 0]
        if len(colonne) != len(set(colonne)):
            return False
    for bi in range(0, 9, 3):
        for bj in range(0, 9, 3):
            bloc = [grille[i][j] for i in range(bi, bi+3) for j in range(bj, bj+3) if grille[i][j] != 0]
            if len(bloc) != len(set(bloc)):
                return False
    return True
# Fonctions utilitaires
def est_valide(grille, ligne, colonne, valeur):
    if valeur in grille[ligne]:
        return False
    for i in range(9):
        if grille[i][colonne] == valeur:
            return False
    debut_ligne = (ligne // 3) * 3
    debut_colonne = (colonne // 3) * 3
    for i in range(debut_ligne, debut_ligne + 3):
        for j in range(debut_colonne, debut_colonne + 3):
            if grille[i][j] == valeur:
                return False
    return True

def get_cases_vides(grille):
    return [(i, j) for i in range(9) for j in range(9) if grille[i][j] == 0]

def nombre_conflits(grille):
    conflits = 0
    for i in range(9):
        ligne = grille[i]
        conflits += sum(ligne.count(v) - 1 for v in range(1, 10) if ligne.count(v) > 1)
    for j in range(9):
        colonne = [grille[i][j] for i in range(9)]
        conflits += sum(colonne.count(v) - 1 for v in range(1, 10) if colonne.count(v) > 1)
    for bi in range(0, 9, 3):
        for bj in range(0, 9, 3):
            bloc = [grille[i][j] for i in range(bi, bi+3) for j in range(bj, bj+3)]
            conflits += sum(bloc.count(v) - 1 for v in range(1, 10) if bloc.count(v) > 1)
    return conflits

def est_resolu(grille):
    return nombre_conflits(grille) == 0 and all(grille[i][j] != 0 for i in range(9) for j in range(9))

def heuristique(grille):
    cases_vides = len(get_cases_vides(grille))
    conflits = nombre_conflits(grille)
    #modérer l’impact des conflits et encourager le remplissage progressif
    return cases_vides + conflits * 0.5

def grille_to_tuple(grille):
    return tuple(tuple(ligne) for ligne in grille)

def afficher_grille(grille, titre="Grille de Sudoku"):
    print(f"\n{titre}:")
    for i in range(9):
        if i % 3 == 0 and i != 0:
            print("-" * 21)
        for j in range(9):
            if j % 3 == 0 and j != 0:
                print("|", end=" ")
            print(grille[i][j] if grille[i][j] != 0 else ".", end=" ")
        print()
def beam_search(grille_initiale, largeur_faisceau=3):
    iterations = 0
    grille = copy.deepcopy(grille_initiale)
    
    for i, j in get_cases_vides(grille):
        valeurs_possibles = [v for v in range(1, 10) if est_valide(grille, i, j, v)]
        if valeurs_possibles:
            grille[i][j] = random.choice(valeurs_possibles)
    #Initialisations
    faisceau = deque([grille]) 
    meilleur_score = nombre_conflits(grille)
    meilleur_grille = copy.deepcopy(grille)
    historique_scores = [meilleur_score]

    while faisceau:
        iterations += 1
        nouveaux_etats = []
        for etat in faisceau:
            cases_vides = get_cases_vides(etat) 
            if not cases_vides:
                if est_resolu(etat):
                    return etat, iterations, historique_scores
                continue
            i, j = random.choice(cases_vides)
            for valeur in range(1, 10):
                if est_valide(etat, i, j, valeur):
                    nouvel_etat = copy.deepcopy(etat)
                    nouvel_etat[i][j] = valeur
                    score = nombre_conflits(nouvel_etat)
                    nouveaux_etats.append((score, nouvel_etat))
                    #trier les etats successeurs
        nouveaux_etats.sort(key=lambda x: x[0])
        faisceau = deque([etat for _, etat in nouveaux_etats[:largeur_faisceau]])
        if faisceau:
            score_actuel = nombre_conflits(faisceau[0])
            historique_scores.append(score_actuel)
            #voir si l’algorithme progresse vers la solution
            if score_actuel < meilleur_score:
                meilleur_score = score_actuel
                meilleur_grille = copy.deepcopy(faisceau[0])
            if est_resolu(meilleur_grille):
                return meilleur_grille, iterations, historique_scores
    return meilleur_grille, iterations, historique_scores

# Algorithme A* avec gestion des doublons et comparaison de coûts
def a_etoile(grille):
    ouvert = []
    ferme = set()
    #associer à chaque état du Sudoku (sous forme de tuple) le meilleur coût g (coût réel depuis l’état initial) jusqu'à présent
    meilleurs_g = {} 

    g = 0
    h = heuristique(grille)
    f = g + h
    compteur = 0

    heapq.heappush(ouvert, (f, h, compteur, grille, g)) 
    meilleurs_g[grille_to_tuple(grille)] = g

    iterations = 0
    while ouvert:
        f, h, _, etat, g = heapq.heappop(ouvert)
        iterations += 1

        etat_tuple = grille_to_tuple(etat)
        if est_resolu(etat):
            return etat, iterations, f

        if etat_tuple in ferme:
            continue

        ferme.add(etat_tuple)

        cases_vides = get_cases_vides(etat)
        
        i, j = cases_vides[0]
        for val in range(1, 10):
            if est_valide(etat, i, j, val):
                nouvel_etat = copy.deepcopy(etat)
                nouvel_etat[i][j] = val
                g_nouveau = g + 1
                h_nouveau = heuristique(nouvel_etat)
                f_nouveau = g_nouveau + h_nouveau

                etat_nouveau_tuple = grille_to_tuple(nouvel_etat)

                # Si nouvel état non vu ou meilleur g
                if etat_nouveau_tuple not in meilleurs_g or g_nouveau < meilleurs_g[etat_nouveau_tuple]:
                    meilleurs_g[etat_nouveau_tuple] = g_nouveau
                    compteur += 1
                    heapq.heappush(ouvert, (f_nouveau, h_nouveau, compteur, nouvel_etat, g_nouveau))
    return grille, iterations, float('inf') 

#zone dans une fenêtre graphique où on peut dessiner
canvas = None
resultats_label = None
grille_actuelle = []

def dessiner_grille(grille):
    cell_size = 40
    canvas.delete("all")
    for i in range(9):
        for j in range(9):
            x0 = j * cell_size
            y0 = i * cell_size
            x1 = x0 + cell_size
            y1 = y0 + cell_size
            canvas.create_rectangle(x0, y0, x1, y1, outline="black", width=2 if i % 3 == 0 or j % 3 == 0 else 1)
            val = grille[i][j]
            if val != 0:
                canvas.create_text(x0 + 20, y0 + 20, text=str(val), font=("Arial", 14))

# Fonction pour créer le graphique
def creer_graphique_comparaison(it_beam, t_beam, it_astar, t_astar):
    ##Crée une figure (fig) et un axe (ax) pour dessiner
    fig, ax = plt.subplots(figsize=(6, 4)) 
    algorithmes = ['Beam Search', 'A*'] 
    iterations = [it_beam, it_astar] 
    temps = [t_beam, t_astar] 

    bar_width = 0.35
    #positions des groupes de barres (Beam Search = 0, A* = 1)
    index = range(len(algorithmes)) 
    ax.bar([i - bar_width/2 for i in index], iterations, bar_width, label='Itérations', color='#54396c')
    ax.bar([i + bar_width/2 for i in index], temps, bar_width, label='Temps (s)', color='#FFFBF4')

    ax.set_xlabel('Algorithme')
    ax.set_ylabel('Valeur')
    ax.set_title('Comparaison des Performances')
    ax.set_xticks(index)
    ax.set_xticklabels(algorithmes)
    ax.legend()

    return fig

# Fonction modifiée pour accepter root comme paramètre
def lancer_resolution(root):  # Ajout de root comme paramètre
    global grille_actuelle, canvas, resultats_label

    grille_copy = copy.deepcopy(grille_actuelle)

    print("\n=== Résolution avec Beam Search ===")
    debut = time.time()
    resultat_beam, it_beam, _ = beam_search(grille_copy)
    t_beam = time.time() - debut
    afficher_grille(resultat_beam, "Grille Finale (Beam Search)")
    dessiner_grille(resultat_beam)

    print("\n=== Résolution avec A* ===")
    debut = time.time()
    resultat_astar, it_astar, _ = a_etoile(grille_copy)
    t_astar = time.time() - debut
    afficher_grille(resultat_astar, "Grille Finale (A*)")
    dessiner_grille(resultat_astar)

    # Mettre à jour le texte des résultats(label)
    resultats_label.config(text=
        f"Résultat Beam Search: {'Résolu' if est_resolu(resultat_beam) else 'Non résolu'} en {it_beam} itérations ({t_beam:.2f}s)\n"
        f"Résultat A*: {'Résolu' if est_resolu(resultat_astar) else 'Non résolu'} en {it_astar} itérations ({t_astar:.2f}s)\n"
    )

    # Ajouter le graphique
    fig = creer_graphique_comparaison(it_beam, t_beam, it_astar, t_astar)
    canvas_graphique = FigureCanvasTkAgg(fig, master=root)  # crée un "canvas" (surface de dessin) pour afficher une figure matplotlib (fig) dans une fenêtre Tkinter.master=root signifie que le widget sera attaché à la fenêtre Tkinter principale root.
    canvas_graphique.get_tk_widget().pack(pady=10) #.pack(pady=10) est une méthode Tkinter pour ajouter le widget à la fenêtre avec un espacement vertical (pady) de 10 pixels autour.
    canvas_graphique.draw()
# Grille d'exemple
grille_exemple = [
    [0, 0, 0, 0, 7, 0, 0, 9, 0],
    [1, 0, 0, 4, 0, 0, 2, 0, 0],
    [0, 0, 2, 0, 0, 5, 0, 0, 0],
    [0, 1, 0, 0, 0, 0, 4, 0, 0],
    [0, 0, 7, 0, 0, 0, 8, 0, 0],
    [0, 0, 6, 0, 0, 0, 0, 1, 0],
    [0, 0, 0, 3, 0, 0, 6, 0, 0],
    [0, 0, 1, 0, 0, 9, 0, 0, 8],
    [0, 2, 0, 0, 1, 0, 0, 0, 0]
]

def lancer_interface(grille):
    global canvas, resultats_label, grille_actuelle

    grille_actuelle = grille
    # Fenêtre principale
    root = tk.Tk()  
    root.title("Solveur Sudoku (Beam Search & A*)")

    canvas = tk.Canvas(root, width=360, height=360)
    canvas.pack()

    dessiner_grille(grille)

    resultats_label = tk.Label(root, text="", justify="left")
    resultats_label.pack(pady=10)

    bouton = ttk.Button(root, text="Résoudre", command=lambda: lancer_resolution(root)) 
    bouton.pack(pady=5)

    root.mainloop()
#Exécution console
def resoudre_sudoku(grille):
    if not verifier_grille_initiale(grille):
        print("Erreur : La grille initiale est invalide (doublons détectés).")
        return
    grille_initiale = copy.deepcopy(grille)

    print("\n--- Résolution avec Beam Search ---")
    debut = time.time()
    resultat_beam, iterations_beam, _ = beam_search(grille_initiale)
    temps_beam = time.time() - debut
    afficher_grille(grille_initiale, "Grille Initiale")
    afficher_grille(resultat_beam, "Grille Finale (Beam Search)")
    print(f"Temps: {temps_beam:.4f} s | Itérations: {iterations_beam} | Conflits: {nombre_conflits(resultat_beam)}")

    print("\n--- Résolution avec A* ---")
    debut = time.time()
    resultat_a_etoile, iterations_a_etoile, _ = a_etoile(grille_initiale)
    temps_a_etoile = time.time() - debut
    afficher_grille(grille_initiale, "Grille Initiale")
    afficher_grille(resultat_a_etoile, "Grille Finale (A*)")
    print(f"Temps: {temps_a_etoile:.4f} s | Itérations: {iterations_a_etoile} | Conflits: {nombre_conflits(resultat_a_etoile)}")
    print("\nLimitations:")
    print("- Beam Search: Rapide, mais peut rester bloqué dans un optimum local.")
    print("- A*: Plus lent, mais explore exhaustivement pour une solution optimale.")


# Lancement
if __name__ == "__main__":
    resoudre_sudoku(grille_exemple)
    lancer_interface(grille_exemple)
