package com.example.NoticiasViews;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tcc20.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class NewsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new NewsAdapter(getNewsList())); // Implemente getNewsList() para obter as notícias

        return view;
        // Inflate the layout for this fragment

    }
    private List<News> getNewsList() {
        // Retorne uma lista simulada de notícias
        List<News> newsList = new ArrayList<>();
        newsList.add(new News("Pequenas e médias empresas crescem 9,4% no terceiro trimestre",
                "No acumulado do ano até setembro, o índice mostra crescimento de 5,1% frente ao mesmo período do ano anterior",
                "https://agenciagentileza.com.br/wp-content/uploads/2021/12/3542817-scaled.jpg",
                "https://exame.com/negocios/pequenas-e-medias-empresas-crescem-94-no-terceiro-trimestre/"));
        // Adicione mais notícias conforme necessário

        newsList.add(new News("Mercado está mais cético com a economia do que há dois meses, diz diretor da Quaest",
                "Pesquisa Genial/Quaest divulgada nesta terça-feira (19) mostrou que a avaliação positiva de investidores sobre o ministro da Fazenda caiu de 65% para 46% entre julho e setembro",
                "https://www.cnnbrasil.com.br/wp-content/uploads/sites/12/Reuters_Direct_Media/BrazilOnlineReportDomesticNews/tagreuters.com2023binary_LYNXMPEJ6P0ZZ-FILEDIMAGE.jpg?w=1220&h=674&crop=1",
                "https://www.cnnbrasil.com.br/economia/mercado-esta-mais-cetico-com-economia-do-que-ha-dois-meses-diz-diretor-da-quaest/"));

        newsList.add(new News("Aposta contra humanos: altos custos trabalhistas valorizam ações de empresas de IA em Wall Street","Além de desenvolvedoras de tecnologia, companhias que apresentam pressões menores em relação à mão de obra - como Tesla e Coca Cola - também se destacam\n" +
                "\n","https://www.cnnbrasil.com.br/wp-content/uploads/sites/12/2023/07/GettyImages-1464561797.jpg?w=1220&h=674&crop=1","https://www.cnnbrasil.com.br/economia/aposta-contra-humanos-altos-custos-trabalhistas-valorizam-acoes-de-empresas-de-ia-em-wall-street/"));

        newsList.add(new News("“Mercado vê agora o que deveria ter visto desde o começo do mandato”, diz presidente do BB",
                "Tarciana Medeiros assumiu o banco prometendo aliar a atuação comercial ao cumprimento de sua função social, o que levou temores a investidores",
                "https://www.cnnbrasil.com.br/wp-content/uploads/sites/12/2023/08/a81t3999-e1691679590327.jpg?w=1170&h=658&crop=1",
                "https://www.cnnbrasil.com.br/economia/mercado-ve-agora-o-que-deveria-ter-visto-desde-o-comeco-do-mandato-diz-presidente-do-bb/"));
        return newsList;
    }
}