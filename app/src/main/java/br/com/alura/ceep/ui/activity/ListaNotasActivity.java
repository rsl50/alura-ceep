package br.com.alura.ceep.ui.activity;

import android.os.Bundle;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerview.adapter.ListaNotasAdapter;

public class ListaNotasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);
        List<Nota> todasNotas = notasDeExemplo();
        configuraRecyclerView(todasNotas);
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        configuraAdapter(todasNotas, listaNotas);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        listaNotas.setAdapter(new ListaNotasAdapter(this, todasNotas));
    }

    private List<Nota> notasDeExemplo() {
        NotaDAO dao = new NotaDAO();

        //for (int i = 1; i <= 10000; i++) {
            //dao.insere(new Nota("Título " + i, "Descrição " + i));
        //}
        dao.insere(new Nota("Primeira nota", "Descrição pequena"),
                new Nota("Segunda nota", "Descrição bem longa da nota sendo maior que a nota anterior"));
        return dao.todos();
    }
}
