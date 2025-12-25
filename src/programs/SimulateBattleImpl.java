package programs;

import com.battle.heroes.army.Army;
import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.GeneratePreset;
import com.battle.heroes.army.programs.PrintBattleLog;
import com.battle.heroes.army.programs.SimulateBattle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class SimulateBattleImpl implements SimulateBattle {
    private PrintBattleLog printBattleLog; // Позволяет логировать. Использовать после каждой атаки юнита

    @Override
    public void simulate(Army playerArmy, Army computerArmy) throws InterruptedException {
        List<Unit> playerUnits = playerArmy.getUnits();
        List<Unit> computerUnits = computerArmy.getUnits();

        while (hasAliveUnits(playerUnits) && hasAliveUnits(computerUnits)) {

            List<Unit> allUnits = new ArrayList<>();
            allUnits.addAll(playerUnits);
            allUnits.addAll(computerUnits);
            allUnits.sort(Comparator.comparingInt(Unit::getBaseAttack).reversed());

            for (Unit unit : allUnits) {
                if (!unit.isAlive()) continue;

                Unit target = unit.getProgram().attack();

                if (printBattleLog != null) {
                    printBattleLog.printBattleLog(unit, target);
                }

                if (!hasAliveUnits(playerUnits) || !hasAliveUnits(computerUnits)) {
                    break;
                }
            }
        }
    }

    private boolean hasAliveUnits(List<Unit> units) {
        for (Unit unit : units) {
            if (unit.isAlive()) return true;
        }
        return false;
    }

    public static class GeneratePresetImpl implements GeneratePreset {

        @Override
        public Army generate(List<Unit> unitList, int maxPoints) {
            Army computerArmy = new Army();
            List<Unit> selectedUnits = new ArrayList<>();
            int currentPoints = 0;

            unitList.sort((u1, u2) -> {
                double eff1 = ((double) u1.getBaseAttack() / u1.getCost()) + ((double) u1.getHealth() / u1.getCost());
                double eff2 = ((double) u2.getBaseAttack() / u2.getCost()) + ((double) u2.getHealth() / u2.getCost());
                return Double.compare(eff2, eff1);
            });

            for (Unit unitType : unitList) {
                int unitsAdded = 0;
                while (unitsAdded < 11 && (currentPoints + unitType.getCost()) <= maxPoints) {
                    Unit newUnit = new Unit(
                            unitType.getName() + " " + unitsAdded,
                            unitType.getUnitType(),
                            unitType.getHealth(),
                            unitType.getBaseAttack(),
                            unitType.getCost(),
                            unitType.getAttackType(),
                            new HashMap<>(), // Пустые бонусы атаки
                            new HashMap<>(), // Пустые бонусы защиты
                            -1, // Координата X (расставит движок)
                            -1  // Координата Y (расставит движок)
                    );


                    newUnit.setProgram(unitType.getProgram());

                    selectedUnits.add(newUnit);
                    currentPoints += unitType.getCost();
                    unitsAdded++;
                }
            }

            computerArmy.setUnits(selectedUnits);
            computerArmy.setPoints(currentPoints);
            return computerArmy;
        }
    }
}