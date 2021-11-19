package peu.example.usandofirebase;
// INSERE VÁRIOS DADOS NO BD (NAO SOBRESCREVE)
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity3 extends AppCompatActivity {

    // Atributo: referência para o nosso banco de dados!
    // Esta referência "aponta" para o nó RAIZ da árvore!
    private DatabaseReference BD = FirebaseDatabase.getInstance().getReference();

    // Atributos que representam os objetos da interface gráfica:
    private EditText txtNome;
    private EditText txtSobrenome;
    private EditText txtIdade;
    private Button btnEnviar;
    //private Button btnListar;
    private TextView lblNome;
    private TextView lblSobrenome;
    private TextView lblIdade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        // Ligando os atributos com a interface gráfica:
        txtNome = findViewById( R.id.txtNome );
        txtSobrenome = findViewById( R.id.txtSobrenome );
        txtIdade = findViewById( R.id.txtIdade );
        btnEnviar = findViewById( R.id.btnEnviar);
        //btnListar = findViewById( R.id.btnListar);
        lblNome = findViewById( R.id.lblNome );
        lblSobrenome = findViewById( R.id.lblSobrenome );
        lblIdade = findViewById( R.id.lblIdade );

        // Criando o escutador do botão enviar
        btnEnviar.setOnClickListener( new EscutadorBotao() );
        //btnListar.setOnClickListener( new EscutadorBotaoListar() );

        // Criando uma referência para o nó "dados" no Firebase:
        DatabaseReference dados = BD.child( "dados-muitos" );

        // Criando e associando um escutador neste nó:
        dados.addValueEventListener( new EscutadorFirebase() );
    }

    private class EscutadorBotao implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            // variáveis para os dados digitados nas caixas
            String nome, sobren;
            int idade;

            // Pegando os dados nas caixas.
            // ATENÇÃO!! não vamos fazer filtros de erros!
            nome = txtNome.getText().toString();
            sobren = txtSobrenome.getText().toString();
            idade = Integer.parseInt( txtIdade.getText().toString() );

            // Criando o objeto Usuario, com os dados acima
            Usuario u = new Usuario( nome, sobren, idade );

            // Referenciando nó principal da tabela
            DatabaseReference dados = BD.child("dados-muitos");

            // Gerando um nó aleatório, que será utilizado como "chave" para
            // os dados deste usuário (como se fosse a "chave" da tabela.
            // OBS:
            //     - método push() :   gera o valor aleatório
            //     - método getKey() : devolve o valor gerado, pra gente poder usar
            String chave = dados.push().getKey();

            // Enfim, gravando os dados deste usuário "debaixo" deste nó gerado:
            dados.child( chave ).setValue( u );

            txtNome.setText("");
            txtSobrenome.setText("");
            txtIdade.setText("");
        }
    }

    private class EscutadorFirebase implements ValueEventListener {

        @Override public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            // Testando se veio dados no dataSnapshot:
            if ( dataSnapshot.exists() ) {
                // Variáveis auxiliares...
                String n, s;
                int i;
                // OK, temos dados!
                // Vamos "varrer" os filhos deste dataSnapshot:

                for (DataSnapshot datasnapUsuario : dataSnapshot.getChildren()) {
                    // A variável "dataSnapUsuario" tem um dataSnapshot com apenas 1 único usuário lido.
                    // Vamos extrair esse usuário!
                    Usuario u = datasnapUsuario.getValue(Usuario.class);

                    // Agora é só exibir os dados deste usuário no toast...
                    n = u.getNome();
                    s = u.getSobrenome();
                    i = u.getIdade();

                    // Colocando os dados do objeto lido na interface gráfica.
                    lblNome.setText(n);
                    lblSobrenome.setText(s);
                    lblIdade.setText(Integer.toString(i));

                    //Toast.makeText(MainActivity3.this, "Nome: " + n + "\nSobrenome: " + s + "\nIdade: " + i, Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }

    }

    /*private class EscutadorBotaoListar implements View.OnClickListener {
        @Override public void onClick(View view) {
            // Criando uma referencia para o nó principal da nossa tabela, "semana26":
            DatabaseReference dados = BD.child("dados-muitos");

            // Criando e associando o escutador do Firebase para este nó.
            // OBS!!! Apenas para uma única leitura.
            // Ou seja, cada vez que apertar o botão, cria o escutador, associa,
            // ele faz seu trabalho, e "morre".
            dados.addListenerForSingleValueEvent(new EscutadorFirebase() );
        }

    }*/
}