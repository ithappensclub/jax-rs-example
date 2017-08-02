package com.welyab.ithappens.tutorials.springboot.jaxrs.data.model.dao;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import java.io.Serializable;
import org.hibernate.Session;

public abstract class Dao<Entity, Pk> {

    private final Class<Entity> entityClass;

    protected Dao(Class<Entity> entityClass) {
        this.entityClass = entityClass;
    }

    public Entity find(Pk pk) {
        return getSession().find(entityClass, pk);
    }

    public void save(Entity entity) {
        getSession().persist(entity);
    }

    public void update(Entity entity) {
        getSession().merge(entity);
    }

    public void delete(Entity entity) {
        getSession().delete(entity);
    }

    public void delete(Serializable pk) {
        Entity e = getSession().getReference(entityClass, pk);
        delete(e);
    }

    public JPQLQuery createJpqlQuery() {
        return new JPAQuery(getSession());
    }

    protected abstract Session getSession();
}
