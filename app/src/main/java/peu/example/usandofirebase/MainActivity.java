package peu.example.usandofirebase;
// INSERE APENAS UM DADO NO BD, SOBRESCREVENDO
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    // Atributo: referência para o nosso banco de dados!
    // Esta referência "aponta" para o nó RAIZ da árvore!
    private DatabaseReference BD = FirebaseDatabase.getInstance().getReference();

    // Atributos que representam os objetos da interface gráfica:
    private EditText txtNome;
    private EditText txtSobrenome;
    private EditText txtIdade;
    private Button btnEnviar;
    private TextView lblNome;
    private TextView lblSobrenome;
    private TextView lblIdade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ligando os atributos com a interface gráfica:
        txtNome = findViewById( R.id.txtNome );
        txtSobrenome = findViewById( R.id.txtSobrenome );
        txtIdade = findViewById( R.id.txtIdade );
        btnEnviar = findViewById( R.id.btnListar);
        lblNome = findViewById( R.id.lblNome );
        lblSobrenome = findViewById( R.id.lblSobrenome );
        lblIdade = findViewById( R.id.lblIdade );

        // Criando o escutador do botão enviar
        btnEnviar.setOnClickListener( new EscutadorBotao() );

        // Criando uma referência para o nó "dados" no Firebase:
        DatabaseReference dados = BD.child( "dado-unico" );

        // Criando e associando um escutador neste nó:
        dados.addValueEventListener( new EscutadorFirebase() );
    }


    // SÓ ADD NO BANCO SOBRESCREVENDO O ANTERIOR
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

            // Vamos ter apenas um nó, "dados", então podemos gravar direto
            BD.child( "dado-unico" ).setValue( u );

            txtNome.setText("");
            txtSobrenome.setText("");
            txtIdade.setText("");
        }
    }

    private class EscutadorFirebase implements ValueEventListener {
        // O método onDataChange é chamado em 2 ocasiões:
        // - assim que o Escutador é criado e associado ao respectivo nó;
        // - sempre que houver alguma alteração nos dados.

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            // Os dados recuperados no Firebase vem dentro de um DataSnapshot.
            // No nosso caso, só vai ter um objeto lá dentro (objeto da classe Usuario).

            // Precisamos testar se veio alguma informação, isto é,
            // se o dataSnapshot existe...

            if ( dataSnapshot.exists() ) {
                // Recuperamos o objeto Usuario que veio dentro do dataSnapshot:
                Usuario u = dataSnapshot.getValue(Usuario.class);

                // Colocando os dados do objeto lido na interface gráfica.
                lblNome.setText(u.getNome());
                lblSobrenome.setText(u.getSobrenome());
                lblIdade.setText(Integer.toString(u.getIdade()));
            }
        }
        // Não trabalharemos com este método. Mas ele tem que existir!!
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    }

}