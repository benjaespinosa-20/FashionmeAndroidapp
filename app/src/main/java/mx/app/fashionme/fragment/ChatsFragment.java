package mx.app.fashionme.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
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

import mx.app.fashionme.R;
import mx.app.fashionme.adapter.ChatsAdapter;
import mx.app.fashionme.interactor.ChatsInteractor;
import mx.app.fashionme.pojo.ChatAssessorClient;
import mx.app.fashionme.presenter.ChatsPresenter;
import mx.app.fashionme.presenter.interfaces.IChatsPresenter;
import mx.app.fashionme.view.interfaces.IChatsView;

public class ChatsFragment extends Fragment implements IChatsView {

    // Miembros UI
    private RecyclerView rvChats;
    private SwipeRefreshLayout srlSwipe;
    private ProgressBar progressBar;
    private LinearLayout llSomeError;
    private TextView tvErrorTitle;
    private TextView tvErrorDescription;
    private Button btnTryAgain;

    private IChatsPresenter presenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View uiRoot = inflater.inflate(R.layout.fragment_assessor_chats_pending, container, false);

        // UI
        rvChats             = uiRoot.findViewById(R.id.rvChatsPending);
        srlSwipe            = uiRoot.findViewById(R.id.srlSwipe);
        progressBar         = uiRoot.findViewById(R.id.progressBar);
        llSomeError         = uiRoot.findViewById(R.id.llError);
        tvErrorTitle        = uiRoot.findViewById(R.id.tvErrorTitle);
        tvErrorDescription  = uiRoot.findViewById(R.id.tvErrorDescription);
        btnTryAgain         = uiRoot.findViewById(R.id.btnTryAgain);

        presenter = new ChatsPresenter(getActivity(), getContext(), this, new ChatsInteractor());

        srlSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.getPendingChats();
                srlSwipe.setRefreshing(false);
            }
        });

        return uiRoot;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getPendingChats();
    }

    @Override
    public void generateLinearLayout() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvChats.setLayoutManager(llm);
    }

    @Override
    public ChatsAdapter createAdapter() {
        return new ChatsAdapter(presenter);
    }

    @Override
    public void initializeAdapter(ChatsAdapter chatsAdapter, ArrayList<ChatAssessorClient> chatAssessorClients) {
        chatsAdapter.setData(chatAssessorClients);
        chatsAdapter.notifyDataSetChanged();
        rvChats.setAdapter(chatsAdapter);
    }

    @Override
    public void emptyList() {
        tvErrorTitle.setText(R.string.no_chats_pending);
        tvErrorDescription.setText(R.string.no_chats_pending_description);
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.getPendingChats();
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
                presenter.getPendingChats();
            }
        });

    }

    @Override
    public void showError(String error) {
        tvErrorTitle.setText("Algo salio mal");
        tvErrorDescription.setText(error);
        btnTryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.getPendingChats();
            }
        });
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
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
    public void hideList() {
        rvChats.setVisibility(View.GONE);
    }

    @Override
    public void showList() {
        rvChats.setVisibility(View.VISIBLE);
    }
}
