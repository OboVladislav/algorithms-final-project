package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.Edge;
import com.battle.heroes.army.programs.UnitTargetPathFinder;

import java.util.*;

public class UnitTargetPathFinderImpl implements UnitTargetPathFinder {

    private static final int WIDTH = 27;
    private static final int HEIGHT = 21;

    @Override
    public List<Edge> getTargetPath(Unit attackUnit, Unit targetUnit, List<Unit> existingUnitList) {
        Set<String> occupiedCells = new HashSet<>();
        for (Unit unit : existingUnitList) {
            if (unit.isAlive() && unit != attackUnit && unit != targetUnit) {
                occupiedCells.add(unit.getxCoordinate() + "," + unit.getyCoordinate());
            }
        }

        Queue<Edge> queue = new LinkedList<>();
        Edge start = new Edge(attackUnit.getxCoordinate(), attackUnit.getyCoordinate());
        queue.add(start);

        Map<Edge, Edge> parentMap = new HashMap<>();
        parentMap.put(start, null);

        boolean[][] visited = new boolean[WIDTH][HEIGHT];
        visited[start.getX()][start.getY()] = true;

        Edge endEdge = null;

        while (!queue.isEmpty()) {
            Edge current = queue.poll();
              
            if (current.getX() == targetUnit.getxCoordinate() &&
                    current.getY() == targetUnit.getyCoordinate()) {
                endEdge = current;
                break;
            }

            int[][] directions = {
                    {0, 1}, {0, -1}, {1, 0}, {-1, 0},
                    {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
            };

            for (int[] dir : directions) {
                int newX = current.getX() + dir[0];
                int newY = current.getY() + dir[1];

                if (isValid(newX, newY) && !visited[newX][newY]) {
                    if (!occupiedCells.contains(newX + "," + newY)) {
                        Edge neighbor = new Edge(newX, newY);
                        visited[newX][newY] = true;
                        parentMap.put(neighbor, current);
                        queue.add(neighbor);
                    }
                }
            }
        }

        List<Edge> path = new ArrayList<>();
        if (endEdge != null) {
            Edge curr = endEdge;
            while (curr != null) {
                path.add(curr);
                curr = parentMap.get(curr);
            }
            Collections.reverse(path);
            if (!path.isEmpty() &&
                    path.get(0).getX() == attackUnit.getxCoordinate() &&
                    path.get(0).getY() == attackUnit.getyCoordinate()) {
                path.remove(0);
            }
        }

        return path;
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT;
    }
}