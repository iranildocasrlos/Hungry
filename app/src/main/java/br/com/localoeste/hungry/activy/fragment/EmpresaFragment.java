package br.com.localoeste.hungry.activy.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import br.com.localoeste.hungry.R;
import br.com.localoeste.hungry.activy.EmpresaActivity;
import br.com.localoeste.hungry.helper.EmpresaFirebase;
import br.com.localoeste.hungry.helper.UsuarioFirebase;
import br.com.localoeste.hungry.model.Empresa;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmpresaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmpresaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // campos do formulário
    private static EditText nomeEmpresa;
    private static EditText emailEmpresa;
    private static EditText enderecoEmpresa;
    private static EditText telefoneEmpresa;
    private static EditText cnpjEmpresa;
    private static Button btnSalvar;
    private static ImageView imageEmpresaOK;
    private EmpresaFirebase empresa;
    private UsuarioFirebase usuarioFirebase;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EmpresaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EmpresaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EmpresaFragment newInstance(String param1, String param2) {
        EmpresaFragment fragment = new EmpresaFragment();
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

        empresa = new EmpresaFirebase();
        usuarioFirebase = new UsuarioFirebase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_empresa, container, false);

        nomeEmpresa = view.findViewById(R.id.nomeEmpresa);
        enderecoEmpresa = view.findViewById(R.id.enderecoEmpresa);
        emailEmpresa = view.findViewById(R.id.emailEmpresa);
        telefoneEmpresa = view.findViewById(R.id.telefoneEmpresa);
        cnpjEmpresa = view.findViewById(R.id.cnpjEmpresa);
        btnSalvar = view.findViewById(R.id.buttonEmpresa);
        imageEmpresaOK = view.findViewById(R.id.imageEmpresaOK);



        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = nomeEmpresa.getText().toString();
                String email = emailEmpresa.getText().toString();
                String endereco = enderecoEmpresa.getText().toString();
                String telefone = telefoneEmpresa.getText().toString();
                String cnpj = cnpjEmpresa.getText().toString();



                empresa.setNome(nome);
                empresa.setEmail(email);
                empresa.setEndereco(endereco);
                empresa.setTelefone(telefone);
                empresa.setCnpj(cnpj);
                empresa.setIdProprietario(usuarioFirebase.getId_Usuario());

                empresa.salvarDados();

                btnSalvar.setEnabled(false);
                btnSalvar.setVisibility(View.INVISIBLE);
                imageEmpresaOK.setVisibility(View.VISIBLE);

                Intent i = new Intent(getActivity(), EmpresaActivity.class);
                startActivity(i);
                try {
                    EmpresaFragment.this.finalize();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }

            }
        });



        return view;

    }


}