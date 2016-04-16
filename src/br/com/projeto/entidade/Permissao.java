package br.com.projeto.entidade;

import java.io.Serializable;
import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TB_PERMISSAO")
@SequenceGenerator(name = "SQ_PERMISSAO", sequenceName = "SQ_PERMISSAO", allocationSize = 1)
public class Permissao extends GenericEntity {

	private static final long serialVersionUID = 8115095014225327677L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SQ_PERMISSAO")
	@Column(name = "ID_PERMISSAO")
	private Long id;

	@Column(name = "DS_DESCRICAO")
	private String descricao;

	@Column(name = "DS_PERMISAO")
	private String nome;

	@ManyToMany
	@JoinTable(name = "TB_CARGO_PERMISSAO", joinColumns = { @JoinColumn(name = "FK_PERMISSAO") }, inverseJoinColumns = {
			@JoinColumn(name = "FK_CARGO") })
	private List<Cargo> cargoList;
	
	public Permissao() {
		this.cargoList = new ArrayList<>();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Cargo> getCargoList() {
		return cargoList;
	}

	public void setCargoList(List<Cargo> cargoList) {
		this.cargoList = cargoList;
	}

	@Override
	public Serializable obterIdentificador() {
		return this.id;
	}

}