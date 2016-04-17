package com.jm.db;

import android.content.Context;

import com.jm.JmPattern;
import com.jm.utility.JmException;

public interface IDao<T> {

	void init(T db, Context context);

	void start(T db);

	void add(JmPattern jp, int index) throws JmException;

	void addNT(JmPattern jp, int index) throws JmException;

	void add(JmPattern jp, int lang, int index) throws JmException;

	void addNT(JmPattern jp, int lang, int index) throws JmException;

	void set(JmPattern jp) throws JmException;

	void delete(int id) throws JmException;

	JmPattern[] get(int type) throws JmException;

	JmPattern[] get(T db, int type) throws JmException;

	JmPattern[] get(T db, String selection, String orderBy) throws JmException;

	JmPattern[] getFromId(long id) throws JmException;

	int max(int type) throws JmException;

	int countAll() throws JmException;

	int count() throws JmException;

	int count(int type) throws JmException;

	String[] getMenu();

	boolean isWritable();
}