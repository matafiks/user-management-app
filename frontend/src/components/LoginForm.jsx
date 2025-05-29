import { useState } from "react";
import { Link } from "react-router-dom";
import { useNavigate } from "react-router-dom";

export default function LoginForm() {
  const [form, setForm] = useState({ username: "", password: "" });
  const [message, setMessage] = useState(null);
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch("http://localhost:8080/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(form),
      });

      const data = await response.json();

      if (response.ok) {
        setMessage("✅ Login successful!");
        localStorage.setItem("token", data.token); // zapisywanie JWT w local storage
        navigate("/dashboard");
      } else {
        setMessage("❌ Login failed: " + (data.message || "Invalid credentials"));
      }
    } catch (error) {
      setMessage("❌ Server error");
    }
  };

  return (
    <div className="container mt-5">
      <h2 className="mb-4">Login</h2>

      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label htmlFor="username" className="form-label">Username</label>
          <input
            type="text"
            name="username"
            id="username"
            className="form-control"
            value={form.username}
            onChange={handleChange}
            required
          />
        </div>

        <div className="mb-3">
          <label htmlFor="password" className="form-label">Password</label>
          <input
            type="password"
            name="password"
            id="password"
            className="form-control"
            value={form.password}
            onChange={handleChange}
            required
          />
        </div>

        <button type="submit" className="btn btn-primary">Login</button>
      </form>

      {message && (
        <div className="alert alert-info mt-3">{message}</div>
      )}

      <p className="mt-3">
        Don't have an account? <Link to="/register">Register</Link>
    </p>
    </div>
  );
}
