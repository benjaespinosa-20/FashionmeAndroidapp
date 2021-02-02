package mx.app.fashionme.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import mx.app.fashionme.R;
import mx.app.fashionme.adapter.ContactsAdapter;
import mx.app.fashionme.interactor.ContactsInteractor;
import mx.app.fashionme.pojo.ChatAssessorClient;
import mx.app.fashionme.presenter.ContactsPresenter;
import mx.app.fashionme.presenter.interfaces.IContactsPresenter;
import mx.app.fashionme.view.AssessorContactsActivity;
import mx.app.fashionme.view.interfaces.IContactsView;

public class ContactsFragment extends Fragment implements IContactsView {

    // Miembros UI
    private RecyclerView rvContacts;
    private SwipeRefreshLayout srlSwipe;
    private ProgressBar progressBar;
    private LinearLayout llSomeError;
    private TextView tvErrorTitle;
    private TextView tvErrorDescription;
    private Button btnTryAgain;

    private IContactsPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layour for this fragment
        View uiRoot = inflater.inflate(R.layout.fragment_assessor_contacts, container, false);

        // UI
        rvContacts          = uiRoot.findViewById(R.id.rvContacts);
        srlSwipe            = uiRoot.findViewById(R.id.srlSwipeContacts);
        progressBar         = uiRoot.findViewById(R.id.progressBar);
        llSomeError         = uiRoot.findViewById(R.id.llError);
        tvErrorTitle        = uiRoot.findViewById(R.id.tvErrorTitle);
        tvErrorDescription  = uiRoot.findViewById(R.id.tvErrorDescription);
        btnTryAgain         = uiRoot.findViewById(R.id.btnTryAgain);

        presenter = new ContactsPresenter(getActivity(), getContext(), this, new ContactsInteractor());

        srlSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getContacts();
                srlSwipe.setRefreshing(false);
            }
        });

        return uiRoot;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getContacts();
    }

    @Override
    public void generateLinearLayout() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvContacts.setLayoutManager(llm);
    }

    @Override
    public ContactsAdapter createAdapter() {
        return new ContactsAdapter(presenter);
    }

    @Override
    public void initializeAdapter(ContactsAdapter adapter, ArrayList<ChatAssessorClient> clients) {
        adapter.setData(clients);
        adapter.notifyDataSetChanged();
        rvContacts.setAdapter(adapter);
    }

    @Override
    public void emptyList() {
        tvErrorTitle.setText(R.string.no_chats);
        tvErrorDescription.setText(R.string.no_chats_description);
        btnTryAgain.setText(R.string.go_to_request_chat);
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((AssessorContactsActivity) Objects.requireNonNull(getActivity())).setupViewPager(1);
            }
        });
    }

    @Override
    public void showOffline() {
        tvErrorTitle.setText("Sin conexion");
        tvErrorDescription.setText("No tienes conexion, por favor revisa que estes conectado a la red");
        btnTryAgain.setText(getString(R.string.try_again));
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.getContacts();
            }
        });

    }

    @Override
    public void showError(String error) {
        tvErrorTitle.setText("Algo salio mal");
        tvErrorDescription.setText(error);
        btnTryAgain.setText(getString(R.string.try_again));
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.getContacts();
            }
        });
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showLinearLayoutError() {
        llSomeError.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLinearLayoutError() {
        llSomeError.setVisibility(View.GONE);
    }

    @Override
    public void showList() {
        rvContacts.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideList() {
        rvContacts.setVisibility(View.GONE);
    }
}
