package in.ac.bits_pilani.goa.ard.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import in.ac.bits_pilani.goa.ard.Adapters.HomeRvAdapter;
import in.ac.bits_pilani.goa.ard.Models.CardType;
import in.ac.bits_pilani.goa.ard.R;
import in.ac.bits_pilani.goa.ard.interfaces.HomeFragmentListener;
import in.ac.bits_pilani.goa.ard.utils.AHC;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment can implement the
 * {@link HomeFragmentListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 * @author Vikramaditya Kukreja
 */
public class HomeFragment extends Fragment {

    /**
     * recyclerView shown in  homeFragment.
     */
    private RecyclerView homeRv;

    /**
     * adapter for recyclerVoew.
     */
    private HomeRvAdapter homeRvAdapter;

    /**
     * the list of cards displayed in recyclerView.
     */
    private List<CardType> cardList;

    /**
     * title of this fragment.
     */
    private String fragmentTitle;

    /**
     * TaG.
     */
    private final String TAG = AHC.TAG + ".fragments." + HomeFragment.class.getSimpleName();

    /**
     * listener.
     */
    private HomeFragmentListener mListener;

    public HomeFragment() {
        // Required empty public constructor.
    }

    public void setCardList(final List<CardType> cardList) {
        this.cardList = cardList;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param fragmentTitle Title for the fragment.
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance(final String fragmentTitle) {
        final HomeFragment fragment = new HomeFragment();
        final Bundle args = new Bundle();
        args.putString(AHC.FRAGMENT_TITLE_KEY, fragmentTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            fragmentTitle = getArguments().getString(AHC.FRAGMENT_TITLE_KEY);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mListener.updateHomeFragment();
        Log.d(TAG, fragmentTitle);
        final View parentHolder = inflater.inflate(R.layout.fragment_home, container, false);

        homeRv = (RecyclerView) parentHolder.findViewById(R.id.fragment_home_rv);
        homeRvAdapter = new HomeRvAdapter();
        if (cardList != null) {
            handleRecyclerView(cardList);
        }

        /*
        CardType card1=new CardType();
        card1.setType(0);
        List<Object> temp= new ArrayList<Object>();
        temp.add(0, (Bitmap)BitmapFactory.decodeResource(getActivity().getApplicationContext().getResources(),
                R.drawable.ic_stat));
        temp.add(1,(String)"Title");
        temp.add(2,(String)"date");
        temp.add(3,(String)"77");
        card1.setValue(temp);

        cardList.add(card1);
        */
        return parentHolder;

    }

    /**
     * Helps in creating the recycclerView in home fragment.
     * @param cardList list of cards shown in recyclerView
     */
    public void handleRecyclerView(final List<CardType> cardList) {
        homeRvAdapter.setCardList(cardList);
        final RecyclerView.LayoutManager mLayoutManager
                = new LinearLayoutManager(getActivity().getApplicationContext());
        homeRv.setLayoutManager(mLayoutManager);
        homeRv.setItemAnimator(new DefaultItemAnimator());
        homeRv.setAdapter(homeRvAdapter);
    }

    @Override
    public void onAttach(final Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragmentListener) {
            mListener = (HomeFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
