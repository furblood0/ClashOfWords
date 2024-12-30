package com.furkan.clashofwords.ui.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.furkan.clashofwords.R;
import com.furkan.clashofwords.ResourceBarHelper;
import com.furkan.clashofwords.databinding.FragmentFriendsBinding;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment {

    private FragmentFriendsBinding binding;
    private ResourceBarHelper resourceBarHelper;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFriendsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Resource Bar'ı başlat ve dinlemeye başla
        initializeResourceBar(view);

        // Örnek arkadaş listesi
        List<Friend> friends = new ArrayList<>();
        friends.add(new Friend("Ahmet", R.drawable.defaultppicon64, true));
        friends.add(new Friend("Mehmet", R.drawable.defaultppicon64, true));
        friends.add(new Friend("Ayşe", R.drawable.defaultppicon64, false));
        friends.add(new Friend("Fatma", R.drawable.defaultppicon64, false));

        // RecyclerView ve Adapter ayarları
        FriendsAdapter adapter = new FriendsAdapter(friends);
        binding.recyclerViewFriends.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerViewFriends.setAdapter(adapter);
    }

    private void initializeResourceBar(View view) {
        View resourceBarView = view.findViewById(R.id.layout_resource_bar);
        resourceBarHelper = new ResourceBarHelper(requireContext(), resourceBarView);
        resourceBarHelper.startListeningForUserResources();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        resourceBarHelper.stopEnergyRegeneration(); // Enerji artışını durdur
        binding = null;
    }
}
