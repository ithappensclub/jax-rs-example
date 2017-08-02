package com.welyab.ithappens.tutorials.springboot.jaxrs.data.model.dao;

import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.types.expr.BooleanExpression;
import com.welyab.ithappens.tutorials.springboot.jaxrs.data.hibernate.HibernateUtil;
import com.welyab.ithappens.tutorials.springboot.jaxrs.data.model.QUser;
import com.welyab.ithappens.tutorials.springboot.jaxrs.data.model.User;
import java.util.List;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Component
public class UserDao extends Dao<User, Long> {

    public UserDao() {
        super(User.class);
    }

    public boolean existsById(Long userId) {
        return createJpqlQuery()
                .from(QUser.user)
                .where(QUser.user.id.eq(userId))
                .exists();
    }

    public List<User> find(
            List<Long> ids,
            List<String> names,
            List<String> emails
    ) {
        JPQLQuery query = createJpqlQuery()
                .from(QUser.user);
        if (!ids.isEmpty()) {
            query.where(QUser.user.id.in(ids));
        }
        if (!names.isEmpty()) {
            BooleanExpression exprs = names
                    .stream()
                    .map(n -> QUser.user.name.like(String.format("%%%s%%", n)))
                    .reduce((e1, e2) -> e1.or(e2))
                    .get();
            query.where(exprs);
        }
        if (!emails.isEmpty()) {
            BooleanExpression exprs = emails
                    .stream()
                    .map(n -> QUser.user.email.like(String.format("%%%s%%", n)))
                    .reduce((e1, e2) -> e1.or(e2))
                    .get();
            query.where(exprs);
        }
        return query.list(QUser.user);
    }

    public List<User> findAll() {
        return createJpqlQuery()
                .from(QUser.user)
                .list(QUser.user);
    }

    @Override
    protected Session getSession() {
        return HibernateUtil.getSession();
    }

    public boolean existsByEmail(String email) {
        return createJpqlQuery()
                .from(QUser.user)
                .where(QUser.user.email.eq(email))
                .exists();
    }

    public boolean existsByUserName(String userName) {
        return createJpqlQuery()
                .from(QUser.user)
                .where(QUser.user.userName.eq(userName))
                .exists();
    }

    public Long getIdByEmail(String email) {
        return createJpqlQuery()
                .from(QUser.user)
                .where(QUser.user.email.eq(email))
                .singleResult(QUser.user.id);
    }

    public Long getIdByUserName(String userName) {
        return createJpqlQuery()
                .from(QUser.user)
                .where(QUser.user.userName.eq(userName))
                .singleResult(QUser.user.id);
    }
}
