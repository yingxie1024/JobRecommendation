package rpc;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import db.MySQLConnection;
import entity.Item;

/**
 * Servlet implementation class ItemHistory
 */
public class ItemHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ItemHistory() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userId = request.getParameter("user_id");
		JSONArray array = new JSONArray();

		MySQLConnection connection = new MySQLConnection();
		Set<Item> items = connection.getFavoriteItems(userId);
		connection.close();

		for (Item item : items) {
			JSONObject obj = item.toJSONObject();
			obj.put("favorite", true);
			array.put(obj);
		}
		RpcHelper.writeJsonArray(response, array);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		MySQLConnection connection = new MySQLConnection();
		JSONObject input = RpcHelper.readJSONObject(request);
		String userId = input.getString("user_id");
		Item item = RpcHelper.parseFavoriteItem(input.getJSONObject("favorite"));

		connection.setFavoriteItems(userId, item);
		connection.close();
		RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		MySQLConnection connection = new MySQLConnection();
		JSONObject input = RpcHelper.readJSONObject(request);
		String userId = input.getString("user_id");
		Item item = RpcHelper.parseFavoriteItem(input.getJSONObject("favorite"));

		connection.unsetFavoriteItems(userId, item.getItemId());
		connection.close();
		RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));
	}

}
