package peu.example.usandofirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity2 extends AppCompatActivity {

    // Atributo: referência para o nosso banco de dados!
    // Esta referência "aponta" para o nó RAIZ da árvore!
    private DatabaseReference BD = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Criando uma referência para o nó "usuarios" da árvore.
        // Isto é, a referência vai "apontar" para o nó "usuarios".
        // Se o nó não existir, ele cria.
        //
        // Atente para o significado do comando abaixo:
        // "O nó usuarios é filho (child) do BD.
        // Como BD aponta para a RAIZ, "usuarios" está debaixo
        // (é filho) da RAIZ.
        DatabaseReference usuarios = BD.child("usuarios");

        // Vamos criar dois objetos da classe Usuario:
        Usuario u1 = new Usuario("Yuuji", "Itadori", 15);
        Usuario u2 = new Usuario("Osamu", "Miya", 17);

        // Vamos gravar o objeto "u1" como filho (child) do nó "001",
        // e o objeto "u2" como filho (child) do nó "002",
        // ambos como nós filhos do nó "usuarios":
        usuarios.child( "001" ).setValue( u1 );
        usuarios.child( "002" ).setValue( u2 );
    }
}