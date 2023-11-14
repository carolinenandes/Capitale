package com.example.tcc20;

import static androidx.core.content.ContentProviderCompat.requireContext;
import static java.security.AccessController.getContext;

import android.content.Context;
import android.net.Uri;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private List<News> newsList;

    public NewsAdapter(List<News> newsList) {
        this.newsList = newsList;

    }
    private void openNewsInWebView(String url, Context context) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = newsList.get(position);
        holder.titleTextView.setText(news.getTitle());
        holder.descriptionTextView.setText(news.getDescription());



        // Carregar imagem usando Glide (ou Picasso, etc.)
        Glide.with(holder.itemView.getContext())
                .load(news.getImageUrl())
                .placeholder(R.drawable.vetro_android) // Imagem de espaço reservado
                .into(holder.imageView);

        // Configurar clique no botão para abrir a URL da notícia
        holder.readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openNewsInWebView(news.getNewsUrl(), view.getContext());

            }
        });



    }


    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView, readMore;  // Certifique-se de que está declarado corretamente
        TextView titleTextView;
        TextView descriptionTextView;


        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            readMore = itemView.findViewById(R.id.readMore);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }


    private void openNewsUrl(String url, android.content.Context context) {
        // Criar um Intent para abrir uma URL
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Verificar se há um navegador disponível para abrir a URL

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        } else {
            Toast.makeText(context, "Nenhum navegador disponível", Toast.LENGTH_SHORT).show();
        }
    }

}
