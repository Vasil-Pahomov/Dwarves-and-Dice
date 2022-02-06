package by.lvr.dwarvesanddice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    DiceView[] diceViews;
    ProgressBar progressBar;
    TextView progressText;
    Game game = new Game();

    SoundPool soundPool;
    int soundIdNext, soundIdFinish, soundIdReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        diceViews = new DiceView[3];
		diceViews[0] = (DiceView)findViewById(R.id.dice1);
        diceViews[1] = (DiceView)findViewById(R.id.dice2);
        diceViews[2] = (DiceView)findViewById(R.id.dice3);

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressText = (TextView)findViewById(R.id.progressText);
		game.init(3, 6);
        progressBar.setMax(game.getCombinationsCount());

        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);

        soundIdNext = soundPool.load(this, R.raw.next,1);
        soundIdFinish = soundPool.load(this, R.raw.finish,1);
        soundIdReset = soundPool.load(this, R.raw.reset,1);
    }

    public void ViewClick(View view) {
        if (game.getPlayedCombinationsCount() == game.getCombinationsCount()) {
            Reset();
            return;
        }
        game.nextCombination();
        if (game.getPlayedCombinationsCount() != game.getCombinationsCount()) {
            soundPool.play(soundIdNext,1,1,0,0,1);
        } else {
            soundPool.play(soundIdFinish,1,1,0,0,1);
        }

        UpdateControls();
    }

    private void UpdateControls() {
        byte[] currCombo = game.getCurrentCombination();
        if (currCombo == null) {
            progressText.setText("");
            progressBar.setProgress(0);
            for (int i=0;i<3;i++) {
                diceViews[i].resetParams();
            }
            return;
        }
        for (int i=0;i<3;i++) {
            diceViews[i].setParams(getResources().getColor(getResources().getIdentifier(String.format("dice%d",currCombo[i]),"color",this.getPackageName())),
                    getResources().getColor(getResources().getIdentifier(String.format("text%d",currCombo[i]),"color",this.getPackageName())),
                    "");
        }
        progressText.setText(String.format(getString(R.string.progress_text_format), game.getPlayedCombinationsCount(), game.getCombinationsCount()));
        progressBar.setProgress(game.getPlayedCombinationsCount());
    }

    private void Reset() {
        soundPool.play(soundIdReset,1,1,0,0,1);
        game.reset();
        UpdateControls();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        game.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        game.restoreState(savedInstanceState);
        UpdateControls();
    }

    public void ResetClick(View view) {
        Reset();
    }
}