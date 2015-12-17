package com.stonewar.appname.manager;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.util.Log;

/**
 * Created by admin on 17.08.2015.
 */
public class FragmentManager {

    private static final String TAG = FragmentManager.class.getName();

    public static void replaceFragment(int containerViewId, Fragment fragment, android.app.FragmentManager fragmentManager, int transactionIn, int transactionOut){
//        String fragmentName = fragment.getClass().getName();
//        Log.d(TAG, "The fragment's name: " + fragmentName);
//        boolean isFragmentPoped = fragmentManager.popBackStackImmediate(fragmentName, 0);
//        Log.d(TAG, "Is poped: "+isFragmentPoped);

//        if(!isFragmentPoped) {
            FragmentTransaction fragTrans = fragmentManager.beginTransaction();
            fragTrans.setCustomAnimations(transactionIn, transactionOut);
//            fragTrans.replace(containerViewId, fragment, fragmentName);
            fragTrans.replace(containerViewId, fragment);
//            fragTrans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//            fragTrans.addToBackStack(fragmentName);
            fragTrans.addToBackStack(null);
            fragTrans.commit();
//        }
    }

    public static Fragment getCurrentFragment(android.app.FragmentManager fragmentManager){
        //Get the top fragment's tag from the stack
        String fragmentTag = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1).getName();
        Log.d(TAG, "Current fragment's tag: "+fragmentTag);
        //Get the fragment with this fragment's name and return it
        return fragmentManager.findFragmentByTag(fragmentTag);
    }

}
