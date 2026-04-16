# TP Java RMI

Ce projet realise le TP RMI visible dans `tp-java.md` avec quatre parties :

- une fabrique d'objets distants (`ShapeFactory`) qui cree des formes RMI
- un mini serveur de jeu (`GameServer`) avec callbacks vers les clients
- une demonstration du passage par copie et par reference
- un gestionnaire de taches distribue

## Structure

- `src/main/java/interfaces` : interfaces distantes partagees
- `src/main/java/server` : implementations serveur et publication dans le registry
- `src/main/java/client` : clients de demonstration


## Lancer le projet

Maven n'etait pas installe dans l'environnement utilise pour la verification, donc le code a ete verifie avec `javac` en compilant dans `target/classes`.

Compilation :

```powershell
New-Item -ItemType Directory -Force target/classes | Out-Null
javac -d target/classes (Get-ChildItem -Recurse -Filter *.java src/main/java | ForEach-Object { $_.FullName })
```

Lancer le serveur :

```powershell
java -cp target/classes server.ServerMain
```

Lancer le client des formes :

```powershell
java -cp target/classes client.ShapeClientMain
```

Lancer un client callbacks :

```powershell
java -cp target/classes client.GameClientMain localhost 1099 ahmed
```

Vous pouvez lancer un second client avec un autre nom pour voir les notifications entre clients.

Lancer la simulation demandee pour la partie 2 :

```powershell
java -cp target/classes client.GameSimulationMain
```

Lancer la demonstration du passage par copie :

```powershell
java -cp target/classes client.VectorClientMain
```

Lancer la demonstration du passage par reference :

```powershell
java -cp target/classes client.CounterClientMain
```

Lancer la demonstration de la partie 4 :

```powershell
java -cp target/classes client.TaskManagerClientMain
```

## Observation partie 3

- `Vector2D` est `Serializable` : le serveur recoit une copie. Si le client modifie `v.x` apres l'appel, cela ne modifie aucun objet distant deja traite.
- `SharedCounter` est `Remote` : le client manipule un stub. Les increments/decrements sont visibles immediatement sur l'objet distant.

## Observation partie 4

- `TaskManager.createTask(...)` joue le role de fabrique et retourne un `TaskHandle` distant.
- `TaskData` circule par copie car il est `Serializable`.
- `TaskHandle` circule par reference sous forme de stub, ce qui permet l'assignation et le suivi de progression en temps reel.
- Les callbacks `TaskCallback` notifient les abonnes lors de la creation et de la completion des taches.
