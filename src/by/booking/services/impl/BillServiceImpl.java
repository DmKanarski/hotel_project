package by.booking.services.impl;

import by.booking.constants.Messages;
import by.booking.dao.impl.BillDao;
import by.booking.dao.impl.RoomDao;
import by.booking.entities.Bill;
import by.booking.entities.Room;
import by.booking.exceptions.DaoException;
import by.booking.exceptions.ServiceException;
import by.booking.services.interfaces.IBillService;
import by.booking.utils.BookingSystemLogger;
import by.booking.utils.ConnectionUtil;
import by.booking.utils.EntityBuilder;
import by.booking.utils.ExceptionHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class BillServiceImpl implements IBillService {

    private static BillServiceImpl instance;

    private BillServiceImpl(){
    }

    public static synchronized BillServiceImpl getInstance(){
        if(instance == null){
            instance = new BillServiceImpl();
        }
        return instance;
    }

    @Override
    public void add(Bill bill) throws ServiceException {
        Connection connection = ConnectionUtil.getConnection();
        try {
            connection.setAutoCommit(false);
            BillDao.getInstance().add(bill);
            connection.commit();
            BookingSystemLogger.getInstance().logError(getClass(), Messages.TRANSACTION_SUCCEEDED);
        } catch (SQLException | DaoException e) {
            ExceptionHandler.getServiceHandler(connection, e, getClass());
        }
    }

    @Override
    public List<Bill> getAll() throws ServiceException {
        return null;
    }

    @Override
    public Bill getById(long billId) throws ServiceException {
        Connection connection = ConnectionUtil.getConnection();
        Bill bill = null;
        try {
            connection.setAutoCommit(false);
            bill = BillDao.getInstance().getById(billId);
            List<Room> roomList = RoomDao.getInstance().getByIdList(bill.getRoomIdList());
            bill = EntityBuilder.buildBill(bill, roomList);
            connection.commit();
            BookingSystemLogger.getInstance().logError(getClass(), Messages.TRANSACTION_SUCCEEDED);
        } catch (SQLException | DaoException e) {
            ExceptionHandler.getServiceHandler(connection, e, getClass());
        }
        return bill;
    }

    @Override
    public void update(Bill bill) throws ServiceException {
        Connection connection = ConnectionUtil.getConnection();
        try {
            connection.setAutoCommit(false);
            BillDao.getInstance().update(bill);
            connection.commit();
            BookingSystemLogger.getInstance().logError(getClass(), Messages.TRANSACTION_SUCCEEDED);
        } catch (SQLException | DaoException e) {
            ExceptionHandler.getServiceHandler(connection, e, getClass());
        }
    }

    @Override
    public void delete(long billId) throws ServiceException {

    }

    public List<Bill> getByUserId(long userId) throws ServiceException {
        Connection connection = ConnectionUtil.getConnection();
        List<Bill> bills = null;
        List<Bill> newBills = new ArrayList<>();
        try {
            connection.setAutoCommit(false);
            bills = BillDao.getInstance().getByUserId(userId);
            for (Bill bill: bills) {
                List<Room> roomList = RoomDao.getInstance().getByIdList(bill.getRoomIdList());
                bill.setRoomList(roomList);
                newBills.add(bill);
            }
            connection.commit();
            BookingSystemLogger.getInstance().logError(getClass(), Messages.TRANSACTION_SUCCEEDED);
        } catch (SQLException | DaoException e) {
            ExceptionHandler.getServiceHandler(connection, e, getClass());
        }
        return newBills;
    }
    

    
}
