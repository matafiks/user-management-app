import { useEffect, useState } from "react";
import UserList from "./UserList";
import { useNavigate } from "react-router-dom";

export default function Dashboard() {
  const [userInfo, setUserInfo] = useState(null);
  const [error, setError] = useState(null);

  const navigate = useNavigate();

  const handleLogout = () => {
    localStorage.removeItem("token");
    navigate("/login");
  };

  useEffect(() => {
    const fetchUserInfo = async () => {
      const token = localStorage.getItem("token");

      try {
        const response = await fetch("http://localhost:8080/users/me", {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
        });

        if (!response.ok) {
          throw new Error("Failed to fetch user info");
        }

        const data = await response.json();
        setUserInfo(data);
      } catch (err) {
        setError(err.message);
      }
    };

    fetchUserInfo();
  }, []);

  if (error) {
    return <div className="alert alert-danger mt-5">{error}</div>;
  }

  if (!userInfo) {
    return <div className="text-center mt-5">Loading...</div>;
  }

  return (
    <div className="container mt-5">
      <button className="btn btn-outline-danger float-end" onClick={handleLogout}>
        Logout
      </button>
      <h2>Welcome, 👑{userInfo.username}!</h2>
      <p>Email: {userInfo.email}</p>

      {userInfo.roles.includes("ROLE_ADMIN") && (
        <div className="mt-4">
          <UserList />
        </div>
      )}
    </div>
  );
}
