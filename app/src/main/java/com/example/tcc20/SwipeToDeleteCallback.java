package com.example.tcc20;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SwipeToDeleteCallback extends ItemTouchHelper.Callback {
    private final adapterProd mAdapter;
    private final ArrayList<Produto> productList;

    public SwipeToDeleteCallback(adapterProd adapter, ArrayList<Produto> productList) {
        mAdapter = adapter;
        this.productList = productList;
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

        // Verifique se a posição é válida e se a lista não está vazia
        if (position != RecyclerView.NO_POSITION && !productList.isEmpty()) {
            mAdapter.removeItem(position);
        }
    }
}





