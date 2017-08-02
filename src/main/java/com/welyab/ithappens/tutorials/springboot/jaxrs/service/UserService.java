package com.welyab.ithappens.tutorials.springboot.jaxrs.service;

import com.welyab.ithappens.tutorials.springboot.jaxrs.data.hibernate.HibernateUtil;
import com.welyab.ithappens.tutorials.springboot.jaxrs.data.model.User;
import com.welyab.ithappens.tutorials.springboot.jaxrs.data.model.dao.UserDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserService {

    @Autowired
    private UserDao userDao;

    public User save(User user) throws UserAlreadyRegisteredException {
        if (user.getId() != null && userDao.existsById(user.getId())) {
            throw new UserAlreadyRegisteredException(user.getId().toString());
        }
        if (user.getEmail() != null && userDao.existsByEmail(user.getEmail())) {
            throw new UserAlreadyRegisteredException(user.getEmail());
        }
        if (user.getUserName() != null && userDao.existsByUserName(user.getUserName())) {
            throw new UserAlreadyRegisteredException(user.getUserName());
        }
        user.setId(null);
        HibernateUtil.beginTransaction();
        userDao.save(user);
        HibernateUtil.commitTransaction();
        return user;
    }

    public User find(Long id) throws UserNotFoundException {
        User user = userDao.find(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        return user;
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public List<User> find(
            List<Long> ids,
            List<String> names,
            List<String> emails
    ) {
        return userDao.find(ids, names, emails);
    }

    public void delete(Long idUser) {
        HibernateUtil.beginTransaction();
        userDao.delete(idUser);
        HibernateUtil.commitTransaction();
    }

    public User update(User user) throws UserAlreadyRegisteredException, UserNotFoundException {
        if (!userDao.existsById(user.getId())) {
            throw new UserNotFoundException(user.getId());
        }
        if (user.getEmail() != null
                && userDao.existsByEmail(user.getEmail())
                && userDao.getIdByEmail(user.getEmail()) != user.getId().longValue()) {
            throw new UserAlreadyRegisteredException(user.getEmail());
        }
        if (user.getUserName() != null
                && userDao.existsByUserName(user.getUserName())
                && userDao.getIdByUserName(user.getUserName()) != user.getId().longValue()) {
            throw new UserAlreadyRegisteredException(user.getUserName());
        }
        HibernateUtil.beginTransaction();
        userDao.update(user);
        HibernateUtil.commitTransaction();
        return user;
    }
}
