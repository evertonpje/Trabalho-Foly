/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import model.Jogo;

/**
 *
 * @author evert
 */
public class JogoDaoJDBC implements InterfaceDao<Jogo> {

	private Connection conn;

	public JogoDaoJDBC() throws Exception {
		this.conn = ConnFactory.getConnection();
	}

	@Override
	public void incluir(Jogo entidade) throws Exception {

		PreparedStatement ps = conn.prepareStatement("INSERT INTO jogos (nome, genero, plataforma, data_lancamento, status, caminho_foto) VALUES (?, ?, ?, ?, ?, ?)");
		ps.setString(1, entidade.getNome());
		ps.setString(2, entidade.getGenero());
		ps.setString(3, entidade.getPlataforma());
		ps.setString(4, entidade.getDataLancamento());
		ps.setString(5, entidade.getStatus());
		ps.setString(6, entidade.getCaminhoFoto());
		ps.execute();
	}

	@Override
	public void editar(Jogo entidade) throws Exception {

		PreparedStatement ps = conn.prepareStatement("UPDATE jogos SET nome=?, genero=?, plataforma=?, data_lancamento = ?, Status = ?, caminho_foto = ? WHERE id=?");
		ps.setString(1, entidade.getNome());
		ps.setString(2, entidade.getGenero());
		ps.setString(3, entidade.getPlataforma());
		ps.setString(4, entidade.getDataLancamento());
		ps.setString(5, entidade.getStatus());
		ps.setString(6, entidade.getCaminhoFoto());
		ps.setInt(7, entidade.getId());
		ps.execute();
	}

	@Override
	public void excluir(Jogo entidade) throws Exception {
		try {
			PreparedStatement ps = conn.prepareStatement("DELETE FROM jogos WHERE id=?");
			ps.setInt(1, entidade.getId());
			ps.execute();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	@Override
	public List<Jogo> listar() throws Exception {

		PreparedStatement ps = conn.prepareStatement("SELECT * FROM jogos");
		ResultSet rs = ps.executeQuery();
		List<Jogo> lista = new ArrayList();
		while (rs.next()) {
			Jogo c = new Jogo();
			c.setId(rs.getInt("id"));
			c.setNome(rs.getString("nome"));
			c.setGenero(rs.getString("genero"));
			c.setPlataforma(rs.getString("plataforma"));
			c.setDataLancamento(rs.getString("data_lancamento"));
			c.setStatus(rs.getString("Status"));
			c.setCaminhoFoto(rs.getString("caminho_foto"));
			lista.add(c);
		}

		return lista;

	}
}
