/*
* @author: João Marcos Teles Silva CB3026787
* Elabore um programa em Java que apresente um frame semelhante ao que
se segue:

2. Deverá criar um banco SQL Server chamado aulajava com as tabelas
conforme esquema abaixo:

3. Acrescente alguns registros, respeitando as chaves e o relacionamento;
4. Estabeleça a conexão utilizando o JDBC;
5. Ao clicar no botão Pesquisar, deverá ser efetuado o select (utilize like) para
“preencher” um recordset e PreparedStatement para fazer o SQL.
6. Os botões Próximo e Anterior devem permitir a navegação pelo recordset
até os limites inicial e final.
*/




package Frame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

import Entidade.Funcionario;
import Conecxao.Conecxao;

public class TelaFuncionario extends JFrame {
    private JTextField nomeField;
    private JButton pesquisarButton, anteriorButton, proximoButton;
    private JTextArea resultadoArea;
    private ResultSet rs;
    private boolean temDados = false;

    // Variáveis para controlar a navegação no ResultSet
    private boolean podeIrParaAnterior = false;
    private boolean podeIrParaProximo = false;

    public TelaFuncionario() {
        setTitle("Pesquisar Funcionário");
        setLayout(new FlowLayout());

        // Componentes da interface gráfica
        nomeField = new JTextField(20);
        pesquisarButton = new JButton("Pesquisar");
        anteriorButton = new JButton("Anterior");
        proximoButton = new JButton("Próximo");
        resultadoArea = new JTextArea(10, 30);
        resultadoArea.setEditable(false);

        // Adiciona componentes à tela
        add(new JLabel("Nome:"));
        add(nomeField);
        add(pesquisarButton);
        add(new JScrollPane(resultadoArea));
        add(anteriorButton);
        add(proximoButton);

        // Define comportamento dos botões
        pesquisarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarFuncionario(nomeField.getText());
            }
        });

        anteriorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navegarFuncionario(-1);  // Navega para o anterior
            }
        });

        proximoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navegarFuncionario(1);  // Navega para o próximo
            }
        });

        // Configura botões
        anteriorButton.setEnabled(false);
        proximoButton.setEnabled(false);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setVisible(true);
    }

    private void buscarFuncionario(String nome) {
        try {
            // Obtenha a conexão através da classe Conecxao
            Connection conn = Conecxao.getConecxao();
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Erro ao conectar com o banco de dados.");
                return;
            }

            // Consulta SQL
            String query = "SELECT f.cod_func, f.nome_func, f.sal_func, c.ds_cargo "
                         + "FROM tbfuncs f "
                         + "JOIN tbcargos c ON f.cod_cargo = c.cd_cargo "
                         + "WHERE f.nome_func LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            stmt.setString(1, "%" + nome + "%");

            rs = stmt.executeQuery();

            // Verifica se há resultados
            if (rs.next()) {
                temDados = true;
                mostrarFuncionario(rs);
                podeIrParaAnterior = false; // No início, não podemos ir para o anterior
                podeIrParaProximo = rs.next(); // Se houver mais registros, podemos ir para o próximo
                rs.previous(); // Volta para a primeira linha
                anteriorButton.setEnabled(false); // Desabilita o botão "Anterior" se não houver registros anteriores
                proximoButton.setEnabled(true); // Habilita o botão "Próximo"
            } else {
                temDados = false;
                JOptionPane.showMessageDialog(this, "Nenhum funcionário encontrado.");
                anteriorButton.setEnabled(false);
                proximoButton.setEnabled(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao realizar a consulta: " + e.getMessage());
        }
    }

    private void mostrarFuncionario(ResultSet rs) {
        try {
            // Exibe o funcionário atual na tela
            int codFunc = rs.getInt("cod_func");
            String nomeFunc = rs.getString("nome_func");
            double salario = rs.getDouble("sal_func");
            String cargo = rs.getString("ds_cargo");

            resultadoArea.setText("Código: " + codFunc + "\n"
                                + "Nome: " + nomeFunc + "\n"
                                + "Salário: " + salario + "\n"
                                + "Cargo: " + cargo);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void navegarFuncionario(int direcao) {
        try {
            if (rs != null) {
                if (direcao == -1 && rs.previous()) { // Navega para o anterior
                    mostrarFuncionario(rs);
                    podeIrParaAnterior = rs.previous(); // Verifica se há mais registros para "Anterior"
                    proximoButton.setEnabled(true); // Sempre habilita o botão "Próximo"
                } else if (direcao == 1 && rs.next()) { // Navega para o próximo
                    mostrarFuncionario(rs);
                    podeIrParaProximo = rs.next(); // Verifica se há mais registros para "Próximo"
                    anteriorButton.setEnabled(true); // Sempre habilita o botão "Anterior"
                }

                // Atualiza a visibilidade dos botões
                anteriorButton.setEnabled(podeIrParaAnterior);
                proximoButton.setEnabled(podeIrParaProximo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new TelaFuncionario();
    }
}
