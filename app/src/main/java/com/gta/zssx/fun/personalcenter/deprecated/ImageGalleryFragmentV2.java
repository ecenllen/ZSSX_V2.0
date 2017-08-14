package com.gta.zssx.fun.personalcenter.deprecated;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gta.utils.helper.GlideUtils;
import com.gta.zssx.R;
import com.gta.zssx.fun.personalcenter.view.page.ImagePreviewView;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/3/24.
 * @since 1.0.0
 */
public class ImageGalleryFragmentV2 extends Fragment {

    private static final String IMAGE_URL = "image";

    ImagePreviewView mTouchImageView;

    public static ImageGalleryFragmentV2 newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(IMAGE_URL, url);
        ImageGalleryFragmentV2 fragment = new ImageGalleryFragmentV2();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.grallery_page_container, container, false);
        mTouchImageView = (ImagePreviewView) rootView.findViewById(R.id.display_picture);
        mTouchImageView.setOnClickListener(v -> getActivity().finish());
        String mUrl = getArguments().getString(IMAGE_URL);
        GlideUtils.loadUserImage(getActivity(), mUrl, mTouchImageView);
        return rootView;
    }
}
