package application.services;

import java.sql.Connection;

import model.exceptions.DBException;

public abstract class BaseService {
    protected final Connection conn;

    protected BaseService(Connection conn) {
        this.conn = conn;
    }

    protected void beginTransaction() throws DBException {
        try {
            conn.setAutoCommit(false);
        } catch (Exception e) {
            throw new DBException("Could not disable auto-commit.", e);
        }
    }

    protected void commit() throws DBException {
        try {
            conn.commit();
        } catch (Exception e) {
            throw new DBException("Commit failed.", e);
        }
    }

    protected void rollback() throws DBException {
        try {
            conn.rollback();
        } catch (Exception e) {
            throw new DBException("Rollback failed.", e);
        }
    }

    protected void endTransaction() throws DBException {
        try {
            conn.setAutoCommit(true);
        } catch (Exception e) {
            throw new DBException("Could not restore auto-commit.", e);
        }
    }
}