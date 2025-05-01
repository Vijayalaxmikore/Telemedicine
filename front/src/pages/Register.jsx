import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const Register = () => {
  const navigate = useNavigate();
  const [isDoctor, setIsDoctor] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
    specialization: '',
    experience: '',
    licenseId: ''
  });
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleInputChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    try {
      const endpoint = isDoctor 
        ? 'http://localhost:5000/api/auth/doctors/register'   // Changed from doctor to doctors
        : 'http://localhost:5000/api/auth/patients/register';
      const response = await axios.post(endpoint, formData);
      
      if (response.data.token) {
        localStorage.setItem('token', response.data.token);
        localStorage.setItem('user', JSON.stringify(response.data.user));
        navigate('/');
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Registration failed');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
      <div className="max-w-md w-full space-y-8 bg-white p-8 rounded-2xl shadow-xl backdrop-blur-sm bg-opacity-90">
        <div>
          <h2 className="text-3xl font-bold text-center bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">
            Create your account
          </h2>
          <div className="mt-6 flex justify-center">
            <div className="flex space-x-4 p-1 bg-gray-100 rounded-lg">
              <button
                onClick={() => setIsDoctor(false)}
                className={`px-6 py-2 rounded-lg transition duration-200 ${
                  !isDoctor ? 'bg-white text-blue-600 shadow-md' : 'text-gray-600 hover:bg-gray-50'
                }`}
              >
                Patient
              </button>
              <button
                onClick={() => setIsDoctor(true)}
                className={`px-6 py-2 rounded-lg transition duration-200 ${
                  isDoctor ? 'bg-white text-blue-600 shadow-md' : 'text-gray-600 hover:bg-gray-50'
                }`}
              >
                Doctor
              </button>
            </div>
          </div>
        </div>

        {error && (
          <div className="bg-red-50 border border-red-200 text-red-600 px-4 py-3 rounded-lg">
            {error}
          </div>
        )}

        <form className="mt-8 space-y-6" onSubmit={handleSubmit}>
          <div className="rounded-md space-y-4">
            <input
              name="name"
              type="text"
              required
              className="w-full px-4 py-3 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition duration-200"
              placeholder="Full Name"
              onChange={handleInputChange}
            />
            <input
              name="email"
              type="email"
              required
              className="w-full px-4 py-3 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition duration-200"
              placeholder="Email address"
              onChange={handleInputChange}
            />
            <input
              name="password"
              type="password"
              required
              className="w-full px-4 py-3 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition duration-200"
              placeholder="Password"
              onChange={handleInputChange}
            />

            {isDoctor && (
              <>
                <input
                  name="specialization"
                  type="text"
                  required
                  className="w-full px-4 py-3 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition duration-200"
                  placeholder="Specialization"
                  onChange={handleInputChange}
                />
                <input
                  name="experience"
                  type="text"
                  required
                  className="w-full px-4 py-3 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition duration-200"
                  placeholder="Years of Experience"
                  onChange={handleInputChange}
                />
                <input
                  name="licenseId"
                  type="text"
                  required
                  className="w-full px-4 py-3 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition duration-200"
                  placeholder="License ID"
                  onChange={handleInputChange}
                />
              </>
            )}
          </div>

          <button
            type="submit"
            disabled={isLoading}
            className="w-full bg-gradient-to-r from-blue-500 to-indigo-600 text-white py-3 rounded-lg hover:from-blue-600 hover:to-indigo-700 transition duration-300 shadow-md hover:shadow-lg disabled:opacity-70"
          >
            {isLoading ? 'Registering...' : 'Register'}
          </button>
        </form>
      </div>
    </div>
  );
};

export default Register;
