package fr.smile.alfresco.graphql.query;

import org.alfresco.service.namespace.QName;

import fr.smile.alfresco.graphql.helper.AbstractQLModel;

public class ContainerNodeQL extends AbstractQLModel {

	private NodeQL node;
	private QName container;

	public ContainerNodeQL(NodeQL node, QName container) {
		super(node.getQueryContext());
		this.node = node;
		this.container = container;
	}

	public NodeQL getNode() {
		return node;
	}
	
	public boolean getIsType() {
		return getNodeService().getType(node.getNodeRefInternal()).equals(container);
	}	
	public boolean getSetType() {
		getNodeService().setType(node.getNodeRefInternal(), container);
		return true;
	}

	public boolean getHasAspect() {
		return getNodeService().hasAspect(node.getNodeRefInternal(), container);
	}	
	public boolean getAddAspect() {
		getNodeService().addAspect(node.getNodeRefInternal(), container, null);
		return true;
	}
	public boolean getRemoveAspect() {
		getNodeService().removeAspect(node.getNodeRefInternal(), container);
		return true;
	}
}