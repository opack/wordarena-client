package com.slamdunk.toolkit.gameparts.gameobjects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.slamdunk.toolkit.gameparts.components.Component;
import com.slamdunk.toolkit.gameparts.components.TouchHandlerPart;
import com.slamdunk.toolkit.gameparts.components.position.BoundsPart;
import com.slamdunk.toolkit.gameparts.components.position.TransformPart;
import com.slamdunk.toolkit.gameparts.creators.GameObjectFactory;
import com.slamdunk.toolkit.gameparts.scene.Scene;

/**
 * Objet du jeu. C'est un simple agrégat de composants.
 * Lorsqu'un nouveau GameObject est créé dynamiquement, penser à appeler
 * la méthode init() après avoir mis à jour les différentes variables de
 * ses composants. En effet init() n'est appelée automatiquement qu'à
 * la création de la scène.
 * Attention : Toujours utiliser les méthode add/remove pour manipuler la liste
 * de GameObjects enfants ou de composants !
 */
public class GameObject {
	private static final String DEFAULT_NAME_PREFIX = "GameObject";
	private static long gameObjectsCount;
	
	public Scene scene;
	public GameObject parent;
	private List<GameObject> children;
	private List<GameObject> readOnlyChildren;
	private Map<Class<? extends Component>, Component> components;
	
	public long id;
	public String name;
	
	public boolean active;
	
	public TransformPart transform;
	public TouchHandlerPart touchHandler;
	
	public GameObject() {
		// Détermine un nom par défaut
		id = gameObjectsCount;
		name = DEFAULT_NAME_PREFIX + id;
		gameObjectsCount++;
		
		// Par défaut, le gameObject est actif
		active = true;
		
		// Crée la liste des enfants
		children = new ArrayList<GameObject>();
		readOnlyChildren = Collections.unmodifiableList(children);
		
		// Crée la table de composants
		components = new LinkedHashMap<Class<? extends Component>, Component>();
		
		// Ajoute le premier composant, qui permet de positionner le GameObject dans le monde
		transform = addComponent(TransformPart.class);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getSimpleName());
		sb.append(" id: ").append(id);
		sb.append(", name :").append(name);
		sb.append(", components : [").append(components.values());
		return sb.toString();
	}
	
	public GameObject createChild() {
		return addChild(GameObjectFactory.create());
	}
	
	public <T extends GameObject> T createChild(Class<T> childClass) {
		return addChild(GameObjectFactory.create(childClass));
	}

	public <T extends GameObject> T addChild(T child) {
		if (this == child) {
			throw new IllegalArgumentException("A GameObject Cannot be a child of itself !");
		}
		child.scene = scene;
		child.parent = this;
		children.add(child);
		return child;
	}
	
	public List<GameObject> getChildren() {
		return readOnlyChildren;
	}
	
	/**
	 * Raccourci pour getChildren().get(0)
	 * @return
	 */
	public GameObject getFirstChild() {
		if (readOnlyChildren.isEmpty()) {
			return null;
		}
		return readOnlyChildren.get(0);
	}
	
	public GameObject findChild(String name) {
		return findChild(GameObject.class, name);
	}
	
	public GameObject findChild(String name, boolean recurse) {
		return findChild(GameObject.class, name, recurse);
	}
	
	/**
	 * Raccourci pour {@link #findChild(Class, String, boolean)} avec recurse=true
	 * @param childClass
	 * @param name
	 * @return
	 */
	public <T extends GameObject> T findChild(Class<T> childClass, String name) {
		return findChild(childClass, name, true);
	}
	
	public <T extends GameObject> T findChild(Class<T> childClass, String name, boolean recurse) {
		if (name != null) {
			// Cherche dans les enfants du GameObject
			for (GameObject child : children) {
				if (name.equals(child.name)) {
					return childClass.cast(child);
				}
			}
			// Demande aux enfants de chercher dans leurs enfants
			if (recurse) {
				T found;
				for (GameObject child : children) {
					found = child.findChild(childClass, name, recurse);
					if (found != null) {
						return found;
					}
				}
			}
		}
		return null;
	}

	/**
	 * Crée une instance du composant de la classe indiquée et l'initialise
	 * en appelant les méthode createDependencies() et reset().
	 * @param componentClass
	 * @return
	 */
	public <T extends Component> T addComponent(Class<T> componentClass) {
		if (hasComponent(componentClass)) {
			throw new IllegalStateException("There must only be one instance of " + componentClass + " in a GameObject, as this Component is marked as unique.");
		}
		
		T component = null;
		try {
			component = componentClass.newInstance();
			component.gameObject = this;
			components.put(component.getClass(), component);
			component.createDependencies();
			component.reset();
		} catch (InstantiationException e) {
			throw new IllegalStateException("Creation of component " + componentClass + " is impossible due to InstantiationException", e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("Creation of component " + componentClass + " is impossible due to IllegalAccessException", e);
		}
		return component;
	}
	
	public boolean removeComponent(Class<? extends Component> componentClass) {
		return (components.remove(componentClass) != null);
	}
	
	public <T extends Component> T getComponent(Class<T> componentClass) {
		return componentClass.cast(components.get(componentClass));
	}
	
	/**
	 * Indique si le GameObject possède un composant ayant la classe passée en argument
	 * @param componentClasses
	 * @return
	 */
	public boolean hasComponent(Class<? extends Component> componentClass) {
		return components.get(componentClass) != null;
	}
	
	/**
	 * Indique si le GameObject possède un composant de chaque classe passée en argument
	 * @param componentClasses
	 * @return
	 */
	public boolean hasComponents(Class<? extends Component>... componentClasses) {
		for (Class<? extends Component> componentClass : componentClasses) {
			if (components.get(componentClass) == null) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Place les valeurs par défaut dans les composants du GameObject 
	 * en appelant la méthode reset() de chacun des composants, 
	 * dans l'ordre où ils ont été ajoutés.
	 * Attention : cette méthode appelle reset() sur chaque composant
	 * et chaque enfant même s'il est inactif.
	 */
	public void reset() {
		for (Component component : components.values()) {
			component.reset();
		}
		for (GameObject child : children) {
			child.reset();
		}
	}

	/**
	 * Initialise les composants du GameObject en appelant la méthode
	 * init() de chacun des composants, dans l'ordre où ils ont
	 * été ajoutés.
	 * Attention : cette méthode appelle init() sur chaque composant
	 * et chaque enfant même s'il est inactif.
	 */
	public void init() {
		for (Component component : components.values()) {
			component.init();
		}
		for (GameObject child : children) {
			child.init();
		}
		touchHandler = getComponent(TouchHandlerPart.class);
	}
	
	/**
	 * Met à jour la physique du GameObject en appelant la méthode
	 * physics() de chacun des composants, dans l'ordre où ils ont
	 * été ajoutés.
	 * Cette méthode peut être appelée plusieurs fois par frame,
	 * donc plusieurs fois avant qu'un appel soit fait à update().
	 * L'appel est fait à intervalles réguliers donc la valeur
	 * de deltaTime est constante d'un appel sur l'autre.
	 * Attention : cette méthode appelle physics() uniquement sur 
	 * les composants et enfants actifs.
	 */
	public void physics(float deltaTime) {
		for (Component component : components.values()) {
			if (component.active) {
				component.physics(deltaTime);
			}
		}
		for (GameObject child : children) {
			if (child.active) {
				child.physics(deltaTime);
			}
		}
	}
	
	/**
	 * Met à jour la logique du GameObject en appelant la méthode
	 * update() de chacun des composants, dans l'ordre où ils ont
	 * été ajoutés.
	 * Attention : cette méthode appelle update() uniquement sur 
	 * les composants et enfants actifs.
	 */
	public void update(float deltaTime) {
		for (Component component : components.values()) {
			if (component.active) {
				component.update(deltaTime);
			}
		}
		for (GameObject child : children) {
			if (child.active) {
				child.update(deltaTime);
			}
		}
	}
	
	/**
	 * Met à jour la logique du GameObject en appelant la méthode
	 * lateUpdate() de chacun des composants, dans l'ordre où ils ont
	 * été ajoutés.
	 * Cette méthode n'est appelée qu'une fois que tous les composants
	 * de tous les gameObjects on eut un appel à leur méthode update().
	 * Attention : cette méthode appelle update() uniquement sur 
	 * les composants actifs.
	 */
	public void lateUpdate() {
		for (Component component : components.values()) {
			if (component.active) {
				component.lateUpdate();
			}
		}
		for (GameObject child : children) {
			if (child.active) {
				child.lateUpdate();
			}
		}
	}
	
	/**
	 * Dessine le GameObject en appelant la méthode
	 * render() de chacun des composants, dans l'ordre où ils ont
	 * été ajoutés.
	 * Attention : cette méthode appelle draw() uniquement sur 
	 * les composants actifs.
	 */
	public void render(Batch batch, ShapeRenderer shapeRenderer) {
		for (Component component : components.values()) {
			if (component.active) {
				component.render(batch, shapeRenderer);
			}
		}
		for (GameObject child : children) {
			if (child.active) {
				child.render(batch, shapeRenderer);
			}
		}
	}
	
	/**
	 * Indique si le point spécifié touche le GameObject
	 * ou un de ses enfants
	 * @param x
	 * @param y
	 * @return
	 */
	public GameObject hit(float x, float y) {
		BoundsPart bounds = getComponent(BoundsPart.class);
		if (bounds != null) {
			if (bounds.contains(x, y)) {
				return this;
			}
		} else if (x == transform.worldPosition.x
				&& y == transform.worldPosition.y) {
			return this;
		}
		GameObject hit;
		for (GameObject child : children) {
			hit = child.hit(x, y);
			if (hit != null) {
				return hit;
			}
		}
		return null;
	}
	
	
	/**
	 * Appelée lorsqu'une touche se fait au-dessus du GameObject
	 * @param x
	 * @param y
	 * @param pointer
	 * @param button
	 * @return true si l'évènement a été géré. Dans ce cas, le GameObject
	 * recevra aussi tous les futurs touchUp() et touchDragged().
	 */
	public boolean touchDown(float x, float y, int pointer, int button) {
		if (touchHandler == null) {
			return false;
		}
		return touchHandler.active && touchHandler.handler.touchDown(x, y, pointer, button);
	}
	
	/**
	 * Appelée lorsqu'une touche est déplacée. Cet appel est fait uniquement
	 * sur le GameObject qui a répondu true à touchDown().
	 * @param x
	 * @param y
	 * @param pointer
	 * @param button
	 * @return true si l'évènement a été géré
	 */
	public boolean touchDragged(float x, float y, int pointer) {
		if (touchHandler == null) {
			return false;
		}
		return touchHandler.active && touchHandler.handler.touchDragged(x, y, pointer);
	}
	
	/**
	 * Appelée lorsqu'une touche est relâchée. Cet appel est fait uniquement
	 * sur le GameObject qui a répondu true à touchDown().
	 * @param x
	 * @param y
	 * @param pointer
	 * @param button
	 * @return true si l'évènement a été géré
	 */
	public boolean touchUp(float x, float y, int pointer, int button) {
		if (touchHandler == null) {
			return false;
		}
		return touchHandler.active && touchHandler.handler.touchUp(x, y, pointer, button);
	}
}
