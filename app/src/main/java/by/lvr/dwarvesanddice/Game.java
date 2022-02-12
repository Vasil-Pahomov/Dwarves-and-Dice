package by.lvr.dwarvesanddice;

/**
 * Created by vasil_000 on 09.02.2015.
 */
import android.os.Bundle;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Game
{
    private List<byte[]> allCombos;
    private int colors;
    private int combinationsCount;
    private int dices;
    private int currentComboIndex;
    private Random rnd = new Random();

    private int calculateCombinationsCount(int dices, int colors)
    {
        return factorial(dices + colors - 1) / factorial(dices) / factorial(colors - 1);
    }

    private void checkInitialized()
    {
        if (!isInitialized()) {
            throw new IllegalStateException("game is not initialized - call init() before!");
        }
    }

    private int factorial(int paramInt)
    {
        if (paramInt == 0) {
            return 1;
        }
        return paramInt * factorial(paramInt - 1);
    }

    private boolean isInitialized()
    {
        return this.combinationsCount > 0;
    }

    public int getCombinationsCount()
    {
        checkInitialized();
        return this.combinationsCount;
    }

    public byte[] getCurrentCombination()
            throws IllegalStateException
    {
        checkInitialized();
        if (currentComboIndex < 0 || currentComboIndex >= this.combinationsCount) {
            return null;
        }
        return this.allCombos.get(currentComboIndex);
    }

    public int getPlayedCombinationsCount()
    {
        checkInitialized();
        return this.currentComboIndex+1;
    }

    public void init(int dices, int colors)
    {
        this.dices = dices;
        this.colors = colors;
        this.combinationsCount = calculateCombinationsCount(dices, colors);
        reset();
    }

    public void nextCombination()
            throws IllegalStateException
    {
        checkInitialized();
        if (getPlayedCombinationsCount() >= this.combinationsCount) {
            throw new IllegalStateException("All combinations are already played");
        }
        this.currentComboIndex++;
    }

    public void previousCombination()
        throws IllegalStateException
    {
        checkInitialized();
        if (getPlayedCombinationsCount() < 0) {
            throw new IllegalStateException("No previous combination available");
        }
        this.currentComboIndex--;
    }
    public void reset()
    {
        checkInitialized();
        this.rnd = new Random();
        this.allCombos = new ArrayList<>(this.combinationsCount);
        byte[] combo = new byte[this.dices];
        for (int i = 0; i < this.dices; i++) {
            combo[i] = 0;
        }
        genCombos(combo,0,1);
        this.currentComboIndex = -1;

        for (int j = 0; j < this.combinationsCount; j++)
        {
            byte[] tmpCombo;
            int newpos = 0;
            newpos = rnd.nextInt(this.getCombinationsCount());
            tmpCombo = this.allCombos.get(newpos);
            this.allCombos.set(newpos, this.allCombos.get(j));
            this.allCombos.set(j,tmpCombo);

            Log.d("ddd", String.format("%s = %s", j, Arrays.toString(this.allCombos.get(j))));
        }
    }

    private void genCombos(byte[] currentCombo, int pos, int maxUsed) {
        if (pos == this.dices) {
            this.allCombos.add(currentCombo.clone());
        } else {
            for (int i=maxUsed; i <= this.colors; i++) {
                currentCombo[pos]= (byte)i;
                genCombos(currentCombo, pos+1,i);
            }
        }
    }

    public void restoreState(Bundle bundle)
    {
        if (bundle == null || !bundle.containsKey("ddd-dices")) {
            return;
        }

        this.dices = bundle.getInt("ddd-dices");
        this.colors = bundle.getInt("ddd-colors");
        int combosRemainingCount = bundle.getInt("ddd-combos");
        this.allCombos.clear();
        for (int i=0;i<combosRemainingCount;i++) {
            this.allCombos.add(bundle.getByteArray(String.format("ddd-combo%d", i)));
        }
        this.currentComboIndex = bundle.getInt("ddd-currentcomboindex");
    }

    public void saveState(Bundle paramBundle)
    {
        if (!isInitialized()) {
            return;
        }
        paramBundle.putInt("ddd-dices", this.dices);
        paramBundle.putInt("ddd-colors", this.colors);
        paramBundle.putInt("ddd-combos", this.allCombos.size());
        for (int i = 0; i < this.allCombos.size(); i++)
        {
            paramBundle.putByteArray(String.format("ddd-combo%d", i), this.allCombos.get(i));
        }
        paramBundle.putInt("ddd-currentcomboindex", this.currentComboIndex);
    }
}
