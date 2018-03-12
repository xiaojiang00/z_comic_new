package com.android.zhhr.presenter;

import android.app.Activity;
import android.content.Intent;

import com.android.zhhr.data.commons.Constants;
import com.android.zhhr.data.entity.Comic;
import com.android.zhhr.data.entity.LoadingItem;
import com.android.zhhr.module.ComicModule;
import com.android.zhhr.ui.view.IIndexView;
import com.android.zhhr.ui.view.IRankView;
import com.android.zhhr.utils.LogUtil;
import com.android.zhhr.utils.ShowErrorTextUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by 张皓然 on 2018/3/9.
 */

public class RankPresenter extends BasePresenter<IRankView>{
    private ComicModule mModel;
    private int page =1;
    private List<Comic> mList;
    private boolean isloadingdata;

    public RankPresenter(Activity context, IRankView view) {
        super(context, view);
        mModel = new ComicModule(context);
        mList = new ArrayList<>();
    }

    public void loadData(String type) {
        if(!isloadingdata){
            mModel.getRankList(getCurrentTime(),type,page, new DisposableObserver<List<Comic>>() {
                @Override
                protected void onStart() {
                    super.onStart();
                    isloadingdata = true;
                }

                @Override
                public void onNext(@NonNull List<Comic> comics) {
                    mList.addAll(comics);
                    List<Comic> temp = new ArrayList<>(mList);
                    if(comics.size()!=0){
                        temp.add(new LoadingItem(true));
                        mView.fillData(temp);
                        isloadingdata = false;
                    }else{
                        temp.add(new LoadingItem(false));
                        mView.fillData(temp);
                    }


                }

                @Override
                public void onError(@NonNull Throwable e) {
                    mView.showErrorView(ShowErrorTextUtil.ShowErrorText(e));
                }

                @Override
                public void onComplete() {
                    mView.getDataFinish();
                    page++;
                }
            });
        }
    }
}
