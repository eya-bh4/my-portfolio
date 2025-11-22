# Sudoku Solver - Fondements de l'IA

## Description

Ce projet consiste en un **solveur automatique de Sudoku** développé en Python dans le cadre de la matière *Fondements de l'Intelligence Artificielle*.  

Le programme utilise deux algorithmes principaux pour résoudre des grilles de Sudoku :

1. **Beam Search** : recherche informée qui explore les meilleurs états à chaque étape selon une largeur de faisceau donnée.  
2. **A*** : recherche optimale avec gestion des doublons et calcul du meilleur coût g pour chaque état du Sudoku.  

Une interface graphique avec **Tkinter** permet de visualiser la grille et les résultats. Un graphique comparatif montre les performances (nombre d’itérations et temps d’exécution) des deux algorithmes.

---

## Fonctionnalités

- Résolution automatique de grilles de Sudoku.
- Détection des redondances dans la grille initiale.
- Interface graphique interactive avec Tkinter.
- Visualisation des performances via matplotlib.
- Comparaison entre Beam Search et A* en termes d'itérations et de temps.
