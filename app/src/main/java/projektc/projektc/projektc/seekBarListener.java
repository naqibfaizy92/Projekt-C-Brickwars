package projektc.projektc.projektc;

import android.widget.SeekBar;

/**
 * Created by Annika on 15.02.2018.
 */

class seekBarListener implements SeekBar.OnSeekBarChangeListener {

    MainActivity mainActivity;

    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        String ort = Integer.toString(seekBar.getId());
        mainActivity.saveStatistic(ort, progress);

    }

    public void onStartTrackingTouch(SeekBar seekBar) {}

    public void onStopTrackingTouch(SeekBar seekBar) {}

}