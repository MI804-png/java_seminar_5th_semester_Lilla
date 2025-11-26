// Vehicle Registration System - JavaScript functionality

document.addEventListener('DOMContentLoaded', function() {
    // Mobile navigation toggle
    const navToggle = document.querySelector('.nav-toggle');
    const navMenu = document.querySelector('.nav-menu');
    
    if (navToggle && navMenu) {
        navToggle.addEventListener('click', function() {
            navMenu.classList.toggle('active');
        });
    }

    // Auto-hide alerts after 5 seconds
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(alert => {
        setTimeout(() => {
            alert.style.opacity = '0';
            setTimeout(() => {
                alert.style.display = 'none';
            }, 300);
        }, 5000);
    });

    // Form validation enhancement
    const forms = document.querySelectorAll('form');
    forms.forEach(form => {
        form.addEventListener('submit', function(e) {
            const requiredFields = form.querySelectorAll('[required]');
            let isValid = true;

            requiredFields.forEach(field => {
                if (!field.value.trim()) {
                    field.classList.add('is-invalid');
                    isValid = false;
                } else {
                    field.classList.remove('is-invalid');
                }
            });

            if (!isValid) {
                e.preventDefault();
                showAlert('Please fill in all required fields', 'danger');
            }
        });
    });

    // Confirm delete actions
    const deleteButtons = document.querySelectorAll('.btn-danger[href*="delete"]');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function(e) {
            if (!confirm('Are you sure you want to delete this item?')) {
                e.preventDefault();
            }
        });
    });

    // Add loading state to buttons - DISABLED to allow form submission
    // The button disabling was preventing forms from submitting properly
    // const submitButtons = document.querySelectorAll('button[type="submit"]');
    // submitButtons.forEach(button => {
    //     button.addEventListener('click', function() {
    //         this.innerHTML = '<i class="spinner"></i> Processing...';
    //         this.disabled = true;
    //     });
    // });

    // Table row highlighting
    const tableRows = document.querySelectorAll('table tbody tr');
    tableRows.forEach(row => {
        row.addEventListener('click', function() {
            tableRows.forEach(r => r.classList.remove('selected'));
            this.classList.add('selected');
        });
    });

    // Initialize charts if Chart.js is available
    if (typeof Chart !== 'undefined') {
        initCharts();
    }
});

// Utility function to show alerts
function showAlert(message, type = 'info') {
    const alertContainer = document.getElementById('alert-container') || createAlertContainer();
    
    const alert = document.createElement('div');
    alert.className = `alert alert-${type}`;
    alert.innerHTML = `
        ${message}
        <button type="button" class="btn-close" onclick="this.parentElement.remove()">×</button>
    `;
    
    alertContainer.appendChild(alert);
    
    // Auto-remove after 5 seconds
    setTimeout(() => {
        alert.remove();
    }, 5000);
}

function createAlertContainer() {
    const container = document.createElement('div');
    container.id = 'alert-container';
    container.style.position = 'fixed';
    container.style.top = '20px';
    container.style.right = '20px';
    container.style.zIndex = '9999';
    document.body.appendChild(container);
    return container;
}

// Initialize charts for statistics page
function initCharts() {
    // Vehicle Brand Chart
    const brandChartCanvas = document.getElementById('brandChart');
    if (brandChartCanvas) {
        const brandData = JSON.parse(brandChartCanvas.dataset.chartData || '{}');
        
        new Chart(brandChartCanvas, {
            type: 'pie',
            data: {
                labels: Object.keys(brandData),
                datasets: [{
                    data: Object.values(brandData),
                    backgroundColor: [
                        '#3498db',
                        '#e74c3c',
                        '#2ecc71',
                        '#f39c12',
                        '#9b59b6',
                        '#1abc9c'
                    ]
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    title: {
                        display: true,
                        text: 'Vehicles by Brand'
                    },
                    legend: {
                        position: 'bottom'
                    }
                }
            }
        });
    }

    // Vehicle Color Chart
    const colorChartCanvas = document.getElementById('colorChart');
    if (colorChartCanvas) {
        const colorData = JSON.parse(colorChartCanvas.dataset.chartData || '{}');
        
        new Chart(colorChartCanvas, {
            type: 'doughnut',
            data: {
                labels: Object.keys(colorData),
                datasets: [{
                    data: Object.values(colorData),
                    backgroundColor: [
                        '#e74c3c',
                        '#3498db',
                        '#ffffff',
                        '#2ecc71',
                        '#f39c12',
                        '#9b59b6'
                    ]
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    title: {
                        display: true,
                        text: 'Vehicles by Color'
                    },
                    legend: {
                        position: 'bottom'
                    }
                }
            }
        });
    }

    // Statistics Bar Chart
    const statsChartCanvas = document.getElementById('statsChart');
    if (statsChartCanvas) {
        const statsData = JSON.parse(statsChartCanvas.dataset.chartData || '{}');
        
        new Chart(statsChartCanvas, {
            type: 'bar',
            data: {
                labels: ['Persons', 'Vehicles', 'Messages'],
                datasets: [{
                    label: 'Count',
                    data: [
                        statsData.totalPersons || 0,
                        statsData.totalVehicles || 0,
                        statsData.totalMessages || 0
                    ],
                    backgroundColor: [
                        '#3498db',
                        '#2ecc71',
                        '#e74c3c'
                    ]
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    title: {
                        display: true,
                        text: 'Database Statistics'
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    }
}

// API helper functions for testing
const API = {
    baseUrl: '/api',
    
    async request(endpoint, options = {}) {
        const url = `${this.baseUrl}${endpoint}`;
        const config = {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        };
        
        try {
            const response = await fetch(url, config);
            if (!response.ok) {
                throw new Error(`HTTP ${response.status}: ${response.statusText}`);
            }
            return await response.json();
        } catch (error) {
            console.error('API request failed:', error);
            showAlert(`API Error: ${error.message}`, 'danger');
            throw error;
        }
    },
    
    async getPersons() {
        return this.request('/persons');
    },
    
    async getPerson(id) {
        return this.request(`/persons/${id}`);
    },
    
    async createPerson(person) {
        return this.request('/persons', {
            method: 'POST',
            body: JSON.stringify(person)
        });
    },
    
    async updatePerson(id, person) {
        return this.request(`/persons/${id}`, {
            method: 'PUT',
            body: JSON.stringify(person)
        });
    },
    
    async deletePerson(id) {
        return this.request(`/persons/${id}`, {
            method: 'DELETE'
        });
    },
    
    async getVehicles() {
        return this.request('/vehicles');
    },
    
    async getVehicleStats() {
        const brands = await this.request('/stats/vehicles-by-brand');
        const colors = await this.request('/stats/vehicles-by-color');
        return { brands, colors };
    }
};

// Export API for global access
window.VehicleRegAPI = API;

// Smooth scrolling for anchor links
document.querySelectorAll('a[href^="#"]').forEach(anchor => {
    anchor.addEventListener('click', function (e) {
        e.preventDefault();
        const target = document.querySelector(this.getAttribute('href'));
        if (target) {
            target.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }
    });
});

// Add loading animation styles
const style = document.createElement('style');
style.textContent = `
    .spinner {
        display: inline-block;
        width: 12px;
        height: 12px;
        border: 2px solid #ffffff;
        border-radius: 50%;
        border-top-color: transparent;
        animation: spin 1s ease-in-out infinite;
    }
    
    @keyframes spin {
        to { transform: rotate(360deg); }
    }
    
    .is-invalid {
        border-color: #e74c3c !important;
        box-shadow: 0 0 0 3px rgba(231, 76, 60, 0.1) !important;
    }
    
    .selected {
        background-color: #e8f4f8 !important;
    }
    
    .btn-close {
        background: none;
        border: none;
        color: inherit;
        font-size: 1.2rem;
        line-height: 1;
        margin-left: auto;
        padding: 0.25rem;
        cursor: pointer;
    }
`;
document.head.appendChild(style);
