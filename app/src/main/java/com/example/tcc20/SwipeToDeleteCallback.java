/*package com.example.tcc20;

import android.database.sqlite.SQLiteDatabase;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SwipeToDeleteCallback extends ItemTouchHelper.Callback {
    private final adapterProd mAdapter;
    private final ArrayList<produtoSelecao> productList;
    private final BancoDeDados banco;

    public SwipeToDeleteCallback(adapterProd adapter, ArrayList<produtoSelecao> productList, BancoDeDados banco) {
        this.mAdapter = adapter;;
        this.productList = productList;
        this.banco = banco;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        // Define os movimentos permitidos (neste caso, apenas o deslize para a esquerda)
        int swipeFlags = ItemTouchHelper.START;
        return makeMovementFlags(0, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        // Implemente esta lógica se você permitir que os itens sejam reordenados por arrastar e soltar
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();

        // Verifique a direção do deslize
        if (direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.RIGHT) {
            produtoSelecao produto = productList.get(position);
            int productIdToDelete = produto.getId();

            mAdapter.deletaProdutoDaDatabase(productIdToDelete);
            mAdapter.notifyDataSetChanged();
        }
    }
}



*/
