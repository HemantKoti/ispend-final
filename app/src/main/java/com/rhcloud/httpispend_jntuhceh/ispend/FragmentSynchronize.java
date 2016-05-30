package com.rhcloud.httpispend_jntuhceh.ispend;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FragmentSynchronize extends Fragment {

    View fragmentSynchronizeView;

    SyncDeviceToServer syncDeviceToServer;
    SyncServerToDevice syncServerToDevice;

    Button buttonSyncDataToServer, buttonSyncDataFromServer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentSynchronizeView  = inflater.inflate(R.layout.fragment_synchronize, container, false);
        fragmentSynchronizeView.setBackgroundColor(Color.WHITE);

        syncDeviceToServer = new SyncDeviceToServer(getContext());
        syncServerToDevice = new SyncServerToDevice(getContext());

        buttonSyncDataToServer = (Button) fragmentSynchronizeView.findViewById(R.id.buttonSyncDataToSever);
        buttonSyncDataToServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncDeviceToServer.syncUser();
            }
        });

        buttonSyncDataFromServer = (Button) fragmentSynchronizeView.findViewById(R.id.buttonSyncDataFromSever);
        buttonSyncDataFromServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syncServerToDevice.syncCategories();
            }
        });

        return fragmentSynchronizeView;
    }
}
