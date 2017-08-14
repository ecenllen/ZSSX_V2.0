package com.gta.utils.module;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/7/6.
 * @since 1.0.0
 */
public interface BaseModule {

    void destroy();

    void displayActivity();

    void displayInFragment(int fragmentContainerResID);

}
