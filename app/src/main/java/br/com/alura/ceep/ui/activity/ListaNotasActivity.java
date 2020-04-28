package br.com.alura.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import br.com.alura.ceep.R;
import br.com.alura.ceep.dao.NotaDAO;
import br.com.alura.ceep.model.Nota;
import br.com.alura.ceep.ui.recyclerview.adapter.ListaNotasAdapter;
import br.com.alura.ceep.ui.recyclerview.adapter.listener.OnItemClickListener;
import br.com.alura.ceep.ui.recyclerview.adapter.listener.OnItemLongClickListener;
import br.com.alura.ceep.ui.recyclerview.helper.callback.NotaItemTouchHelperCallback;

import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CHAVE_POSICAO;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_ALTERA_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.CODIGO_REQUISICAO_INSERE_NOTA;
import static br.com.alura.ceep.ui.activity.NotaActivityConstantes.POSICAO_INVALIDA;

public class ListaNotasActivity extends AppCompatActivity {

    public static final String TITULO_APPBAR = "Notas";
    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);

        setTitle(TITULO_APPBAR);

        List<Nota> todasNotas = pegaTodasNotas();
        configuraRecyclerView(todasNotas);
        configuraBotaoInsereNota();
    }

    private void configuraBotaoInsereNota() {
        TextView botaoInsereNota = findViewById(R.id.lista_notas_insere_nota);
        botaoInsereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaiParaFormularioNotaActivityInsere();
            }
        });
    }

    private void vaiParaFormularioNotaActivityInsere() {
        Intent iniciaFormularioNota = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        startActivityForResult(iniciaFormularioNota, CODIGO_REQUISICAO_INSERE_NOTA);
    }

    private List<Nota> pegaTodasNotas() {
        NotaDAO dao = new NotaDAO();
        for (int i = 1; i <= 10; i++) {
            dao.insere(new Nota("Título " + i, "Descrição " + i));
        }
        return dao.todos();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (ehResultadoInsereNota(requestCode, data)) {
            if (resultadoOk(resultCode)) {
                //Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                Nota notaRecebida = (Nota) data.getExtras().getParcelable(CHAVE_NOTA);
                adiciona(notaRecebida);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Ocorreu um problema na inclusão da nota", Toast.LENGTH_SHORT).show();
            }
        }

        if (ehResultadoAlteraNota(requestCode, data)) {
            if (resultadoOk(resultCode)) {
                Nota notaRecebida = (Nota) data.getExtras().getParcelable(CHAVE_NOTA);
                int posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);

                if (ehPosicaoValida(posicaoRecebida)) {
                    altera(notaRecebida, posicaoRecebida);
                } else {
                    Toast.makeText(this, "Ocorreu um problema na alteração da nota", Toast.LENGTH_SHORT).show();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void altera(Nota notaRecebida, int posicaoRecebida) {
        new NotaDAO().altera(posicaoRecebida, notaRecebida);
        adapter.altera(posicaoRecebida, notaRecebida);
    }

    private void adiciona(Nota notaRecebida) {
        new NotaDAO().insere(notaRecebida);
        adapter.adiciona(notaRecebida);
    }

    private boolean ehPosicaoValida(int posicaoRecebida) {
        return posicaoRecebida > POSICAO_INVALIDA;
    }

    private boolean ehResultadoAlteraNota(int requestCode, @Nullable Intent data) {
        return ehCodigoRequisicaoAlteraNota(requestCode) && temNota(data);
    }

    private boolean ehCodigoRequisicaoAlteraNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_ALTERA_NOTA;
    }

    private boolean ehResultadoInsereNota(int requestCode, @Nullable Intent data) {
        return ehCodigoRequisicaoInsereNota(requestCode) && temNota(data);
    }

    private boolean temNota(@Nullable Intent data) {
        return data != null && data.hasExtra(CHAVE_NOTA);
    }

    private boolean resultadoOk(int resultCode) {
        return resultCode == Activity.RESULT_OK;
    }

    private boolean ehCodigoRequisicaoInsereNota(int requestCode) {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA;
    }

    private void configuraRecyclerView(List<Nota> todasNotas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        configuraAdapter(todasNotas, listaNotas);
        configuraItemTouchHelper(listaNotas);
    }

    private void configuraItemTouchHelper(RecyclerView listaNotas) {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new NotaItemTouchHelperCallback(adapter));
        itemTouchHelper.attachToRecyclerView(listaNotas);
    }

    private void configuraAdapter(List<Nota> todasNotas, RecyclerView listaNotas) {
        adapter = new ListaNotasAdapter(this, todasNotas);
        listaNotas.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota, int posicao) {
                vaiParaFormularioNotaActivityAltera(nota, posicao);
            }
        });

        adapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Nota nota, int posicao) {
                Intent abreFormularioComNota = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
                abreFormularioComNota.putExtra(CHAVE_NOTA, nota);
                abreFormularioComNota.putExtra(CHAVE_POSICAO, posicao);
                //startActivityForResult(abreFormularioComNota, CODIGO_REQUISICAO_ALTERA_NOTA);
                Toast.makeText(ListaNotasActivity.this, nota.getDescricao(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void vaiParaFormularioNotaActivityAltera(Nota nota, int posicao) {
        Intent abreFormularioComNota = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        abreFormularioComNota.putExtra(CHAVE_NOTA, nota);
        abreFormularioComNota.putExtra(CHAVE_POSICAO, posicao);
        startActivityForResult(abreFormularioComNota, CODIGO_REQUISICAO_ALTERA_NOTA);
    }
}
