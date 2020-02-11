package br.com.alura.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerview.adapter.ListaNotasAdapter;

public class ListaNotasActivity extends AppCompatActivity {

    private List<Nota> todasNotas;
    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);
        todasNotas = notasDeExemplo();
        configuraRecyclerView(todasNotas);

        TextView botaoInsereNota = findViewById(R.id.lista_notas_insere_nota);
        botaoInsereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iniciaFormularioNota = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
                startActivityForResult(iniciaFormularioNota, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == 2 && data.hasExtra("nota")) {
            //Nota notaRecebida = (Nota) data.getSerializableExtra("nota");
            Nota notaRecebida = (Nota) data.getExtras().getParcelable("nota");
            new NotaDAO().insere(notaRecebida);
            adapter.adiciona(notaRecebida);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        configuraAdapter(todasNotas, listaNotas);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);
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
