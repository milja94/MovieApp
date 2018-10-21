package com.example.milja.movieapp.ui.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.milja.movieapp.R;
import com.example.milja.movieapp.ui.fragments.BaseFragment;
import com.example.milja.movieapp.ui.fragments.MoviesFragment;
import com.example.milja.movieapp.ui.fragments.NavigationFragment;
import com.example.milja.movieapp.utils.Constants;

public class MainActivity extends AppCompatActivity implements BaseFragment.OnSwitchFragmentListener{
    //STA BI BOLJE URADILA
    // Srediti toolbar bolje i staviti ga na druge ekrane, odraditi backpress kako treba, srediti dizajn da lici na nesto :)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setFragment(new MoviesFragment(), Constants.FR_NAVIGATION);
    }


    /**
     * Method for replacing current active fragment with the one in the arguments
     *
     * @param fragment - fragment to show
     * @param tag      - fragment tag/name
     */
    private void setFragment(Fragment fragment, String tag) {

        try {
            FragmentManager.BackStackEntry backStackEntry = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1);
            if (backStackEntry != null) {
                BaseFragment previouslyActiveFragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(backStackEntry.getName());
                if (previouslyActiveFragment != null && previouslyActiveFragment.getActivity() != null && previouslyActiveFragment.getActivity().getCurrentFocus() != null) {
                    previouslyActiveFragment.getActivity().getCurrentFocus().clearFocus();
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        transaction.add(R.id.main_frame_layout, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();




    }

    @Override
    public void onSwitchFragment(Fragment fragment, String fragmentTag) {
        setFragment(fragment,fragmentTag);
    }
}
