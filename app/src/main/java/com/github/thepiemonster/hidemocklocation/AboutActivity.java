package com.github.thepiemonster.hidemocklocation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;

import java.util.List;

import com.github.thepiemonster.hidemocklocation.R;
import com.drakeet.about.AbsAboutActivity;
import com.drakeet.about.Card;
import com.drakeet.about.Category;
import com.drakeet.about.Contributor;
import com.drakeet.about.Recommendation;
import com.drakeet.about.License;
import com.drakeet.about.OnRecommendationClickedListener;


public class AboutActivity extends AbsAboutActivity
        implements OnRecommendationClickedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOnRecommendationClickedListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreateHeader(@NonNull ImageView icon, @NonNull TextView slogan, @NonNull TextView version) {
        icon.setImageResource(R.mipmap.ic_launcher);
        slogan.setText(R.string.about_slogan);
        version.setText("v" + BuildConfig.VERSION_NAME);
    }

    @Override
    protected void onItemsCreated(@NonNull List<Object> items) {
        items.add(new Category(getString(R.string.about_introduction_title)));
        items.add(new Card(getString(R.string.about_introduction_content)));

        items.add(new Category(getString(R.string.about_developer_title)));
        items.add(new Contributor(R.drawable.avatar_pie, getString(R.string.about_developer_name_pie), getString(R.string.about_developer_description_pie), getString(R.string.about_developer_url_pie)));

        /*items.add(new Category(getString(R.string.about_recommendation_title)));
        items.add(new Recommendation(
                0, "Pure Writer",
                "https://raw.githubusercontent.com/PureWriter/about-page/master/images/app_writer.png",
                "com.drakeet.purewriter",
                "Description",
                "https://play.google.com/store/apps/details?id=com.drakeet.purewriter",
                "2017-10-09 16:46:57",
                "2017-10-09 16:46:57", 2.93, true)
        );*/

        items.add(new Category(getString(R.string.about_opensource_title)));
        items.add(new License("About Page", "drakeet", License.APACHE_2, "https://github.com/drakeet/about-page"));
        items.add(new License("MultiType", "drakeet", License.APACHE_2, "https://github.com/drakeet/MultiType"));

        items.add(new Category(getString(R.string.about_legal_title)));
        items.add(new Card(getString(R.string.about_legal_content)));
    }

    @Override
    public boolean onRecommendationClicked(@NonNull View itemView, @NonNull Recommendation recommendation) {
        Toast.makeText(this, "" + recommendation.appName, Toast.LENGTH_SHORT).show();
        return false;
    }
}
