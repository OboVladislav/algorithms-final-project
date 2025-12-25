package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;

import java.util.*;

public class GeneratePresetImpl implements GeneratePreset {

    @Override
    public Army generate(List<Unit> unitList, int maxPoints) {
        System.out.println("GeneratePresetImpl: Start generating army...");

        Army computerArmy = new Army();
        List<Unit> selectedUnits = new ArrayList<>();
        int currentPoints = 0;

        unitList.sort((u1, u2) -> {
            double eff1 = ((double) u1.getBaseAttack() / u1.getCost()) + ((double) u1.getHealth() / u1.getCost());
            double eff2 = ((double) u2.getBaseAttack() / u2.getCost()) + ((double) u2.getHealth() / u2.getCost());
            return Double.compare(eff2, eff1);
        });

        Set<String> occupiedCoords = new HashSet<>();
        Random random = new Random();

        for (Unit unitType : unitList) {
            int unitsAdded = 0;

            while (unitsAdded < 11 && (currentPoints + unitType.getCost()) <= maxPoints) {

                int coordX;
                int coordY;
                do {
                    coordX = random.nextInt(3); // Столбцы 0, 1, 2
                    coordY = random.nextInt(21); // Строки 0..20
                } while (occupiedCoords.contains(coordX + "," + coordY));

                occupiedCoords.add(coordX + "," + coordY);

                Unit newUnit = new Unit(
                        unitType.getName() + " " + unitsAdded,
                        unitType.getUnitType(),
                        unitType.getHealth(),
                        unitType.getBaseAttack(),
                        unitType.getCost(),
                        unitType.getAttackType(),
                        new HashMap<>(),
                        new HashMap<>(),
                        coordX, // <-- Передаем вычисленный X
                        coordY  // <-- Передаем вычисленный Y
                );

                newUnit.setProgram(unitType.getProgram());

                selectedUnits.add(newUnit);
                currentPoints += unitType.getCost();
                unitsAdded++;
            }
        }

        System.out.println("GeneratePresetImpl: Generated " + selectedUnits.size() + " units.");
        computerArmy.setUnits(selectedUnits);
        computerArmy.setPoints(currentPoints);
        return computerArmy;
    }
}