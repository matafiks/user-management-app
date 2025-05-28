import { useEffect, useState } from "react";

export default function UserList() {
  const [users, setUsers] = useState([]);
  const [error, setError] = useState(null);
  const [editUser, setEditUser] = useState(null);

  useEffect(() => {
    const fetchUsers = async () => {
      const token = localStorage.getItem("token");

      try {
        const response = await fetch("http://localhost:8080/users", {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });

        if (!response.ok) {
          throw new Error("Access denied or failed to fetch users.");
        }

        const data = await response.json();
        setUsers(data);
      } catch (err) {
        setError(err.message);
      }
    };

    fetchUsers();
  }, []);

  const handleDelete = async (id) => {
    const confirm = window.confirm("Are you sure you want to delete this user?");
    if (!confirm) return;

    const token = localStorage.getItem("token");

    try {
      const response = await fetch(`http://localhost:8080/users/${id}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });

      if (response.ok) {
        setUsers(users.filter((u) => u.id !== id));
      } else {
        throw new Error("Failed to delete user.");
      }
    } catch (err) {
      setError(err.message);
    }
  };

  const handleEditClick = (user) => {
    setEditUser({ ...user });
  };

  const handleEditChange = (e) => {
    setEditUser({ ...editUser, [e.target.name]: e.target.value });
  };

  const handleEditSubmit = async (e) => {
    e.preventDefault();
    const token = localStorage.getItem("token");

    try {
      const response = await fetch(`http://localhost:8080/users/${editUser.id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          username: editUser.username,
          email: editUser.email,
          roles: editUser.roles,
        }),
      });

      if (response.ok) {
        setUsers(users.map((u) => (u.id === editUser.id ? editUser : u)));
        setEditUser(null);
      } else {
        throw new Error("Failed to update user.");
      }
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <div className="container mt-5">
      <h2>User List</h2>
      {error && <div className="alert alert-danger">{error}</div>}

      <table className="table table-striped mt-3">
        <thead>
          <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Email</th>
            <th>Roles</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user) => (
            <tr key={user.id}>
              <td>{user.id}</td>
              <td>{user.username}</td>
              <td>{user.email}</td>
              <td>{[...user.roles].join(", ")}</td>
              <td>
                <button className="btn btn-warning btn-sm me-2" onClick={() => handleEditClick(user)}>
                  Edit
                </button>
                <button className="btn btn-danger btn-sm" onClick={() => handleDelete(user.id)}>
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {editUser && (
  <form onSubmit={handleEditSubmit} className="mt-4">
    <h4>Edit User</h4>

    <div className="mb-2">
      <label>Username</label>
      <input
        name="username"
        className="form-control"
        value={editUser.username}
        onChange={handleEditChange}
      />
    </div>

    <div className="mb-2">
      <label>Email</label>
      <input
        name="email"
        className="form-control"
        value={editUser.email}
        onChange={handleEditChange}
      />
    </div>

    <div className="mb-3">
      <label>Roles</label>
      <div>
        {["ROLE_USER", "ROLE_ADMIN"].map((role) => (
          <div className="form-check form-check-inline" key={role}>
            <input
              className="form-check-input"
              type="checkbox"
              id={`role-${role}`}
              value={role}
              checked={editUser.roles.includes(role)}
              onChange={(e) => {
                const updatedRoles = e.target.checked
                  ? [...editUser.roles, role]
                  : editUser.roles.filter((r) => r !== role);

                setEditUser({ ...editUser, roles: updatedRoles });
              }}
            />
            <label className="form-check-label" htmlFor={`role-${role}`}>
              {role.replace("ROLE_", "")}
            </label>
          </div>
        ))}
      </div>
    </div>

    <button type="submit" className="btn btn-success me-2">Save</button>
    <button className="btn btn-secondary" onClick={() => setEditUser(null)}>Cancel</button>
  </form>
)}

    </div>
  );
}
