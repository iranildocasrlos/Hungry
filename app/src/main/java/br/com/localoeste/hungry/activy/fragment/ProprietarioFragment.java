package br.com.localoeste.hungry.activy.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.helper.UsuarioFirebase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProprietarioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProprietarioFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static EditText editNomeProprietario;
    private static EditText editEmailProprietario;
    private static EditText editEnderecoProprietario;
    private static EditText editTelefoneProprietario;
    private static EditText editCpfProprietario;
    private static Button botaoSalvar;
    private static ImageView imageOK;
    private UsuarioFirebase usuario;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProprietarioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProprietarioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProprietarioFragment newInstance(String param1, String param2) {
        ProprietarioFragment fragment = new ProprietarioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

        }

        usuario = new UsuarioFirebase();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_proprietario, container, false);
        // Inflate the layout for this fragment
        editNomeProprietario = view.findViewById(R.id.nomeEmpresa);
        editEmailProprietario = view.findViewById(R.id.emailEmpresa);
        editEnderecoProprietario = view.findViewById(R.id.enderecoEmpresa);
        editTelefoneProprietario = view.findViewById(R.id.telefoneEmpresa);
        editCpfProprietario = view.findViewById(R.id.cpfProprietario);
        botaoSalvar = view.findViewById(R.id.btnSalvarProp);
        imageOK =  view.findViewById(R.id.imageOK);


        botaoSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nomeDigitado = editNomeProprietario.getText().toString();
                String emailDigitado = editEmailProprietario.getText().toString();
                String enderecoDigitado = editEnderecoProprietario.getText().toString();
                String telefoneDigitado = editTelefoneProprietario.getText().toString();
                String cpfDigitado = editCpfProprietario.getText().toString();

                usuario.setNome(nomeDigitado);
                usuario.setEmail(emailDigitado);
                usuario.setEndereco(enderecoDigitado);
                usuario.setTelefone(telefoneDigitado);
                usuario.setCpf(cpfDigitado);

                usuario.atualizarDados();
                botaoSalvar.setEnabled(false);
                botaoSalvar.setVisibility(View.INVISIBLE);
                imageOK.setVisibility(View.VISIBLE);

            }
        });



        return view;

    }







}