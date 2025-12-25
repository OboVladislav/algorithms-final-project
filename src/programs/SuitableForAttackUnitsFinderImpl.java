package programs;

import com.battle.heroes.army.Unit;
import com.battle.heroes.army.programs.SuitableForAttackUnitsFinder;

import java.util.ArrayList;
import java.util.List;

public class SuitableForAttackUnitsFinderImpl implements SuitableForAttackUnitsFinder {

    @Override
    public List<Unit> getSuitableUnits(List<List<Unit>> unitsByRow, boolean isLeftArmyTarget) {
        List<Unit> suitableUnits = new ArrayList<>();

        for (List<Unit> row : unitsByRow) {
            List<Unit> aliveUnits = new ArrayList<>();
            for (Unit unit : row) {
                if (unit != null && unit.isAlive()) {
                    aliveUnits.add(unit);
                }
            }

            if (aliveUnits.isEmpty()) {
                continue;
            }

            if (isLeftArmyTarget) {

                Unit rightMost = aliveUnits.get(0);
                for (Unit u : aliveUnits) {
                    // Используем getxCoordinate()
                    if (u.getxCoordinate() > rightMost.getxCoordinate()) {
                        rightMost = u;
                    }
                }
                suitableUnits.add(rightMost);
            } else {

                Unit leftMost = aliveUnits.get(0);
                for (Unit u : aliveUnits) {
                    if (u.getxCoordinate() < leftMost.getxCoordinate()) {
                        leftMost = u;
                    }
                }
                suitableUnits.add(leftMost);
            }
        }
        return suitableUnits;
    }
}