package com.example.milja.movieapp.ui.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {
    /* switch fragment listener , called when any child fragment wants to switch itself */
    private OnSwitchFragmentListener callBack;

    /* Switch fragment interface, used for signaling main activity to add another fragment */
    public interface OnSwitchFragmentListener {
        void onSwitchFragment(Fragment fragment, String fragmentTag);
    }

    /* public method available to all children fragments to call main activity
     * to add another fragment */
    protected void switchFragment(Fragment fragment, String fragmentTag) {
        callBack.onSwitchFragment(fragment, fragmentTag);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            callBack = (OnSwitchFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnSwitchFragmentListener");
        }
    }
}
