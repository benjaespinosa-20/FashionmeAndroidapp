package mx.app.fashionme.ui.modules.acquire.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.SkuDetails;
import com.google.android.material.snackbar.Snackbar;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import mx.app.fashionme.R;
import mx.app.fashionme.adapter.AcquireAdapter;
import mx.app.fashionme.adapter.viewHolder.RowViewHolder;
import mx.app.fashionme.data.models.acquire.SkuDetailsModel;
import mx.app.fashionme.di.component.Injectable;
import mx.app.fashionme.ui.app.config.BillingAgentConfig;
import mx.app.fashionme.ui.modules.acquire.contracts.AcquireContract;
import mx.app.fashionme.ui.modules.home.enums.HomeEnum;
import mx.app.fashionme.utils.Constants;

public class AcquireFragment extends DialogFragment implements AcquireContract.IAcquireView,
        Injectable, RowViewHolder.OnButtonClickListener {

    @BindView(R.id.screen_wait)
    ProgressBar screen_wait;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.error_text_view)
    TextView error_text_view;

    @BindView(R.id.list)
    RecyclerView rv_list;

    Unbinder unbinder;

    /**
     * Instancia de acquire adapter
     */
    private AcquireAdapter acquireAdapter;

    /**
     * Lista de skus
     */
    private List<SkuDetailsModel> mListSkus = new ArrayList<>();

    /**
     *
     */
    private String skuType = "";

    /**
     * Instancia inyectada de presenter
     */
    @Inject
    AcquireContract.IAcquirePresenter presenter;
    /**
     * Instancia de billing agent config
     */
    @Inject
    BillingAgentConfig mBillingAgentConfig;

    private View root;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            skuType = bundle.getString(Constants.TYPE, HomeEnum.PURCHASE_NONE.name());
        }
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.acquire_fragment, container, false);
        unbinder = ButterKnife.bind(this, root);
        initView();
        presenter.setView(this);
        presenter.getSkus(HomeEnum.valueOf(skuType));
        return root;
    }

    private void initView() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        toolbar.setTitle(R.string.purchase);
        acquireAdapter = new AcquireAdapter(mListSkus, this);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(llm);
        rv_list.setAdapter(acquireAdapter);
    }

    /**
     * Metodo para llenar recycler view con data obtenida
     */
    @Override
    public void populateData(List<SkuDetailsModel> skus) {
        mListSkus.addAll(skus);
        acquireAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgress() {
        screen_wait.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        screen_wait.setVisibility(View.GONE);
    }

    @Override
    public void onError(String error) {
        Snackbar.make(root, error, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onButtonClicked(SkuDetailsModel sku) {
        try {
            SkuDetails skuDetails = new SkuDetails(sku.getOriginalJson());
            mBillingAgentConfig.launchBillingFlow(getActivity(), skuDetails);
        } catch (JSONException e) {
            onError("No se pudo realizar la compra");
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroy();
        unbinder.unbind();
        mListSkus.clear();
    }

}
