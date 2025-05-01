import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { Formik, Form, Field } from 'formik';
import * as Yup from 'yup';
import { useAuth } from '../context/AuthContext';

const LoginSchema = Yup.object().shape({
  email: Yup.string()
    .email('Invalid email address')
    .required('Email is required'),
  password: Yup.string()
    .min(6, 'Password must be at least 6 characters')
    .required('Password is required'),
});

function Login() {
  const navigate = useNavigate();
  const { login } = useAuth();
  const [userType, setUserType] = useState('patient');
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleSubmit = async (values) => {
    setError('');
    setIsLoading(true);
    try {
      console.log('Login attempt with:', values); // Debug log
      const endpoint = userType === 'doctor'
        ? 'http://localhost:5000/api/auth/doctors/login'
        : 'http://localhost:5000/api/auth/patients/login';
      // Pass the endpoint to the login function
      const success = await login(values, userType, endpoint);
      if (success) {
        navigate('/');
      } else {
        setError('Invalid credentials');
      }
    } catch (err) {
      console.error('Login error details:', err); // Debug log
      setError('An error occurred during login');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="max-w-md mx-auto mt-10 bg-white p-8 rounded-2xl shadow-xl backdrop-blur-sm bg-opacity-90">
      <h2 className="text-3xl font-bold text-center mb-8 bg-gradient-to-r from-blue-600 to-indigo-600 bg-clip-text text-transparent">Login</h2>
      
      <div className="flex justify-center mb-8">
        <div className="flex space-x-4 p-1 bg-gray-100 rounded-lg">
          <button
            className={`px-6 py-2 rounded-lg transition duration-200 ${
              userType === 'patient'
                ? 'bg-white text-blue-600 shadow-md'
                : 'text-gray-600 hover:bg-gray-50'
            }`}
            onClick={() => setUserType('patient')}
          >
            Patient
          </button>
          <button
            className={`px-6 py-2 rounded-lg transition duration-200 ${
              userType === 'doctor'
                ? 'bg-white text-blue-600 shadow-md'
                : 'text-gray-600 hover:bg-gray-50'
            }`}
            onClick={() => setUserType('doctor')}
          >
            Doctor
          </button>
        </div>
      </div>

      <Formik
        initialValues={{ email: '', password: '' }}
        validationSchema={LoginSchema}
        onSubmit={handleSubmit}
      >
        {({ errors, touched }) => (
          <Form className="space-y-6">
            <div>
              <Field
                name="email"
                type="email"
                placeholder="Email"
                className="w-full px-4 py-3 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition duration-200"
              />
              {errors.email && touched.email && (
                <div className="text-red-500 text-sm mt-2">{errors.email}</div>
              )}
            </div>

            <div>
              <Field
                name="password"
                type="password"
                placeholder="Password"
                className="w-full px-4 py-3 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent transition duration-200"
              />
              {errors.password && touched.password && (
                <div className="text-red-500 text-sm mt-2">{errors.password}</div>
              )}
            </div>

            {error && (
              <div className="text-red-500 text-sm text-center bg-red-50 py-2 rounded-lg">{error}</div>
            )}

            <button
              type="submit"
              disabled={isLoading}
              className="w-full bg-gradient-to-r from-blue-500 to-indigo-600 text-white py-3 rounded-lg hover:from-blue-600 hover:to-indigo-700 transition duration-300 shadow-md hover:shadow-lg disabled:opacity-70"
            >
              {isLoading ? 'Logging in...' : 'Login'}
            </button>
          </Form>
        )}
      </Formik>

      <p className="mt-6 text-center text-gray-600">
        Don't have an account?{' '}
        <Link to="/register" className="text-blue-600 hover:text-blue-700 font-medium">
          Register here
        </Link>
      </p>
    </div>
  );
}

export default Login;
