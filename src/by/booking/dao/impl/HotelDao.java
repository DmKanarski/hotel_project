package by.booking.dao.impl;

import by.booking.constants.Messages;
import by.booking.dao.interfaces.IHotelDao;
import by.booking.entities.Hotel;
import by.booking.utils.*;
import by.booking.exceptions.DaoException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HotelDao implements IHotelDao {

    private final String ADD_QUERY = "INSERT INTO HOTELS (LOCATION_ID, HOTEL_NAME) VALUES(" +
            "(SELECT LOCATION_ID FROM LOCATIONS WHERE COUNTRY = ? AND CITY = ?), ?)";
    private final String GET_BY_ID_QUERY = "SELECT H.*, L.* " +
            "FROM HOTELS H " +
            "JOIN LOCATIONS L ON H.LOCATION_ID = L.LOCATION_ID WHERE H.HOTEL_ID = ?";
//    private final String GET_ALL_QUERY = "SELECTROOM * FROM CITIES WHERE";
//    private final String UPDATE_QUERY = "UPDATE CITIES SET COUNTRY = ?, CITY = ? WHERE ID = ?";
//    private final String DELETE_QUERY = "UPDATE CITIES SET STATUS = 'deleted' WHERE ID = ?";


    private static HotelDao instance = null;

    private HotelDao() {
    }

    public static HotelDao getInstance() {
        if (instance == null) {
            instance = new HotelDao();
        }
        return instance;
    }

    @Override
    public Hotel add(Hotel hotel) throws DaoException {
        Connection connection = ConnectionUtil.getConnection();
        ResultSet resultSet = null;
        try (PreparedStatement stm = connection.prepareStatement(ADD_QUERY,
                Statement.RETURN_GENERATED_KEYS)) {
            stm.setString(1, hotel.getCountry());
            stm.setString(2, hotel.getCity());
            stm.setString(3, hotel.getName());
            stm.executeUpdate();
            resultSet = stm.getGeneratedKeys();
            resultSet.next();
            hotel.setId(resultSet.getLong(1));
        } catch (SQLException e) {
            BookingSystemLogger.getInstance().logError(getClass(), Messages.ADD_HOTEL_EXCEPTION);
            throw new DaoException(Messages.ADD_HOTEL_EXCEPTION, e);
        } finally {
            ClosingUtil.close(resultSet);
        }
        return hotel;
    }

    @Override
    public Hotel getById(long id) throws DaoException{
        Hotel hotel = null;
        Connection connection = ConnectionUtil.getConnection();
        ResultSet resultSet = null;
        try (PreparedStatement stm = connection.prepareStatement(GET_BY_ID_QUERY)) {
            stm.setLong(1, id);
            resultSet = stm.executeQuery();
            resultSet.next();
            hotel = EntityBuilder.parseHotel(resultSet);
        } catch (SQLException e) {
            BookingSystemLogger.getInstance().logError(getClass(), Messages.GET_HOTEL_EXCEPTION);
            throw new DaoException(Messages.GET_HOTEL_EXCEPTION, e);
        } finally {
            ClosingUtil.close(resultSet);
        }
        return hotel;
    }

    @Override
    public void update(Hotel hotel) throws DaoException{

    }

    @Override
    public void delete(Hotel hotel) throws DaoException{

    }

    public List<Hotel> getAll() {
        List<Hotel> list = new ArrayList<>();
        return list;
    }

    public Hotel getByHotelName(String country, String city) {
        Hotel hotel = new Hotel();
        return hotel;
    }
}