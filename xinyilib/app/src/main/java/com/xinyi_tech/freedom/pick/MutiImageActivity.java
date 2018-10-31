package com.xinyi_tech.freedom.pick;

import android.support.v7.app.AppCompatActivity;

public class MutiImageActivity extends AppCompatActivity {

  /*  @BindView(R.id.simg_1)
    SuperMutiImageView mSimg1;
    @BindView(R.id.simg_2)
    SuperMutiImageView mSimg2;
    @BindView(R.id.simg_3)
    SuperMutiImageView mSimg3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muti_image);
        ButterKnife.bind(this);

        String[] paths = {"/storage/emulated/0/Android/data/com.xinyi_tech.freedom/cache/photo/PickCompressImage/1528351510894_659.JPEG",
                "/storage/emulated/0/Android/data/com.xinyi_tech.freedom/cache/photo/PickCompressImage/1528351518868_84.jpg",
                "/storage/emulated/0/Android/data/com.xinyi_tech.freedom/cache/photo/PickCompressImage/1528351518930_88.jpg",
                "/storage/emulated/0/Android/data/com.xinyi_tech.freedom/cache/photo/PickCompressImage/1528351526464_403.JPEG",
                "/storage/emulated/0/Android/data/com.xinyi_tech.freedom/cache/photo/PickCompressImage/1528351519772_586.jpg",
                "/storage/emulated/0/Pictures/1527732237966.jpg"};

        ArrayList<MediaModel> mediaModels = new ArrayList<>();

        String[] ss = {"测试1", "测试2", "测试3", "测试4"};
        for (String s : ss) {
            final MediaModel localMedia = new MediaModel(MediaType.ofImage());
            localMedia.setPlaceHolderRes(R.drawable.ic_camera);
            localMedia.setMediaName(s);
            localMedia.setMustSelect(true);
            mediaModels.add(localMedia);
        }

        final SuperMutiImageView.Builder builder = new SuperMutiImageView.Builder(this)
                .selectMode(SelectMode.SELECTMODE_EDIT)
                .requestCode(1)
                .maxSelectCount(6);
        mSimg1.setupParams(builder);

        final SuperMutiImageView.Builder builder1 = new SuperMutiImageView.Builder(this)
                .selectMode(SelectMode.SELECTMODE_LOOK)
                .requestCode(10)
                .isOnlyCamera(true)
                .maxSelectCount(6);
        mSimg2.setupParams(builder1);
        mSimg2.setDataPaths(Arrays.asList(paths));

        final SuperMutiImageView.Builder builder2 = new SuperMutiImageView.Builder(this)
                .selectMode(SelectMode.SELECTMODE_PLACEHOLDER)
                .requestCode(20)
                .isOnlyGrallery(true)
                .maxSelectCount(6);
        mSimg3.setupParams(builder2);
        mSimg3.setDatas(mediaModels);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mSimg1.onActivityResult(requestCode, resultCode, data);
        mSimg2.onActivityResult(requestCode, resultCode, data);
        mSimg3.onActivityResult(requestCode, resultCode, data);

    }

    @OnClick(R.id.btn)
    public void onViewClicked() {
        XinYiLog.e(mSimg1.getDataPaths().toString());
        XinYiLog.e(mSimg1.getDatas().toString());


    }*/
}
