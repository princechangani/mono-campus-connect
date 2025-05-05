import React, { useState } from 'react';
import {
    Container,
    Paper,
    TextField,
    Button,
    Typography,
    Box,
    Link,
    Alert,
    Select,
    MenuItem,
} from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { authService } from '../services/authService';

const Register = () => {
    const [formData, setFormData] = useState({
        email: '',
        password: '',
        confirmPassword: '',
        firstName: '',
        lastName: '',
        role: 'STUDENT',
        department: '',
        semester: '',
    });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        if (formData.password !== formData.confirmPassword) {
            setError('Passwords do not match');
            setLoading(false);
            return;
        }

        try {
            await authService.register(formData);
            navigate('/login');
        } catch (err) {
            setError(err.response?.data?.message || 'Registration failed. Please try again.');
        } finally {
            setLoading(false);
        }
    };

    const departments = [
        { value: 'CSE', label: 'Computer Science & Engineering' },
        { value: 'ECE', label: 'Electronics & Communication Engineering' },
        { value: 'MECH', label: 'Mechanical Engineering' },
        { value: 'CIVIL', label: 'Civil Engineering' },
        { value: 'EEE', label: 'Electrical & Electronics Engineering' },
        { value: 'IT', label: 'Information Technology' },
    ];

    const roles = [
        { value: 'STUDENT', label: 'Student' },
        { value: 'FACULTY', label: 'Faculty' },
        { value: 'ADMIN', label: 'Admin' },
    ];

    return (
        <Container maxWidth="sm">
            <Box sx={{ mt: 8, mb: 4 }}>
                <Paper elevation={3} sx={{ p: 4 }}>
                    <Typography variant="h4" component="h1" align="center" gutterBottom>
                        Register
                    </Typography>
                    
                    {error && (
                        <Alert severity="error" sx={{ mb: 2 }}>
                            {error}
                        </Alert>
                    )}

                    <form onSubmit={handleSubmit}>
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            id="firstName"
                            label="First Name"
                            name="firstName"
                            autoComplete="given-name"
                            value={formData.firstName}
                            onChange={handleChange}
                        />
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            id="lastName"
                            label="Last Name"
                            name="lastName"
                            autoComplete="family-name"
                            value={formData.lastName}
                            onChange={handleChange}
                        />
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            id="email"
                            label="Email Address"
                            name="email"
                            autoComplete="email"
                            value={formData.email}
                            onChange={handleChange}
                        />
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            name="password"
                            label="Password"
                            type="password"
                            id="password"
                            autoComplete="new-password"
                            value={formData.password}
                            onChange={handleChange}
                        />
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            name="confirmPassword"
                            label="Confirm Password"
                            type="password"
                            id="confirmPassword"
                            value={formData.confirmPassword}
                            onChange={handleChange}
                        />
                        <Select
                            margin="normal"
                            required
                            fullWidth
                            label="Role"
                            name="role"
                            value={formData.role}
                            onChange={handleChange}
                        >
                            {roles.map((role) => (
                                <MenuItem key={role.value} value={role.value}>
                                    {role.label}
                                </MenuItem>
                            ))}
                        </Select>
                        {formData.role === 'STUDENT' && (
                            <>
                                <Select
                                    margin="normal"
                                    required
                                    fullWidth
                                    label="Department"
                                    name="department"
                                    value={formData.department}
                                    onChange={handleChange}
                                >
                                    <MenuItem value="">Select Department</MenuItem>
                                    {departments.map((dept) => (
                                        <MenuItem key={dept.value} value={dept.value}>
                                            {dept.label}
                                        </MenuItem>
                                    ))}
                                </Select>
                                <Select
                                    margin="normal"
                                    required
                                    fullWidth
                                    label="Semester"
                                    name="semester"
                                    value={formData.semester}
                                    onChange={handleChange}
                                >
                                    <MenuItem value="">Select Semester</MenuItem>
                                    {[1, 2, 3, 4, 5, 6, 7, 8].map(sem => (
                                        <MenuItem key={sem} value={sem}>
                                            Semester {sem}
                                        </MenuItem>
                                    ))}
                                </Select>
                            </>
                        )}
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2 }}
                            disabled={loading}
                        >
                            {loading ? 'Registering...' : 'Register'}
                        </Button>
                        <Box sx={{ textAlign: 'center' }}>
                            <Link href="/login" variant="body2">
                                Already have an account? Sign In
                            </Link>
                        </Box>
                    </form>
                </Paper>
            </Box>
        </Container>
    );
};

export default Register;
