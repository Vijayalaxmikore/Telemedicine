
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

function Navbar() {
  const { user, logout } = useAuth();

  return (
    <nav className="bg-white bg-opacity-90 backdrop-blur-sm shadow-lg sticky top-0 z-50">
      <div className="container mx-auto px-6">
        <div className="flex justify-between items-center py-4">
          <Link to="/" className="text-2xl font-bold bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent hover:from-blue-700 hover:to-indigo-700 transition duration-300">
            TeleMed
          </Link>
          {user ? (
            <div className="flex items-center space-x-6">
              <Link to="/doctors" className="text-gray-700 hover:text-blue-600 transition duration-200">
                Doctors
              </Link>
              <Link to="/profile" className="text-gray-700 hover:text-blue-600 transition duration-200">
                Profile
              </Link>
              <button
                onClick={logout}
                className="bg-gradient-to-r from-red-500 to-red-600 text-white px-5 py-2 rounded-lg hover:from-red-600 hover:to-red-700 transition duration-300 shadow-md hover:shadow-lg"
              >
                Logout
              </button>
            </div>
          ) : (
            <div className="flex items-center space-x-6">
              <Link to="/login" className="text-gray-700 hover:text-blue-600 transition duration-200">
                Login
              </Link>
              <Link
                to="/register"
                className="bg-gradient-to-r from-blue-500 to-indigo-600 text-white px-5 py-2 rounded-lg hover:from-blue-600 hover:to-indigo-700 transition duration-300 shadow-md hover:shadow-lg"
              >
                Register
              </Link>
            </div>
          )}
        </div>
      </div>
    </nav>
  );
}

export default Navbar;